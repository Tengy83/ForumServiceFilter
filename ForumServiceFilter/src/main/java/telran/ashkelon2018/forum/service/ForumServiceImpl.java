package telran.ashkelon2018.forum.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredentials;
import telran.ashkelon2018.forum.dao.ForumRepository;
import telran.ashkelon2018.forum.domain.Comment;
import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.dto.DatePeriodDto;
import telran.ashkelon2018.forum.dto.NewCommentDto;
import telran.ashkelon2018.forum.dto.NewPostDto;
import telran.ashkelon2018.forum.errors.ForumBadRequestException;
import telran.ashkelon2018.forum.errors.ForumNotFaundException;

@Service
public class ForumServiceImpl implements ForumService {
	
	private static final Logger logger = LoggerFactory.getLogger(ForumServiceImpl.class);
	
	private boolean logEnable;
	
	@Autowired
	ForumRepository repository;
	
	@Autowired
	AccountConfiguration accountConfiguration;
	
	// ManagedAttribute
	
	@ManagedAttribute
	public boolean isLogEnable() {
		return logEnable;
	}
	@ManagedAttribute
	public void setLogEnable(boolean logEnable) {
		this.logEnable = logEnable;
	}
	
	// Override methods
	
	@Override
	public Post addNewPost(NewPostDto newPost, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		Post post = convertToPost(newPost, credentials.getLogin());
		repository.save(post);
		return post;
	}
	
	private Post convertToPost(NewPostDto newPost, String authorLogin) {		
		return new Post(newPost.getTitle(), newPost.getContent(), authorLogin, newPost.getTags());
	}

	@Override
	public Post getPost(String id) {
		return repository.findById(id).orElseThrow(ForumNotFaundException::new);
	}

	@Override
	public Post removePost(String id) {
		Post post = getPost(id);
		repository.deleteById(id);
		return post;
	}

	@Override
	public Post updatePost(String id, NewPostDto newPost) {
		Post post = getPost(id);		
		post.setTitle(newPost.getTitle());
		post.setContent(newPost.getContent());
		post.setTags(newPost.getTags());		
		repository.save(post);
		return post;
	}

	@Override
	public boolean addLike(String id, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		Post post = getPost(id);
		boolean addLike = post.addLike(credentials.getLogin());
		repository.save(post);		
		return addLike;
	}

	@Override
	public Post addComment(String id, NewCommentDto newCommentDto, String token) {
		Post post = getPost(id);
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		Comment comment = new Comment(credentials.getLogin(), newCommentDto.getMessage());
		post.addComment(comment);
		repository.save(post);
		return post;
	}	

	@Override
	public Post updareComment(String idPost, String idComment, NewCommentDto newCommentDto) {
		Post post = getPost(idPost);
		Comment comment = post.getComments().stream().filter(c->c.getId().equals(idComment)).findFirst().orElse(null);
		if (comment == null) {
			throw new ForumBadRequestException();
		}
		comment.setMessage(newCommentDto.getMessage());
		repository.save(post);
		return post;
	}
	
	@Override
	public Post removeComment(String idPost, String idComment) {
		Post post = getPost(idPost);
		Comment comment = post.getComments().stream().filter(c->c.getId().equals(idComment)).findFirst().orElse(null);
		if (comment == null || !post.getComments().remove(comment)) {
			throw new ForumBadRequestException();
		}
		repository.save(post);
		return post;
	}
	
	@Override
	public boolean addLikeComment(String idPost, String idComment, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		Post post = getPost(idPost);
		Comment comment = post.getComments().stream().filter(c->c.getId().equals(idComment)).findFirst().orElse(null);
		if (comment == null) {
			throw new ForumBadRequestException();
		}
		boolean addLike = comment.addLike(credentials.getLogin());
		post.addComment(comment);
		repository.save(post);
		return addLike;
	}	

	@Override
	public boolean addTag(String id, String tag) {
		if (tag == null) {
			throw new ForumBadRequestException();
		}
		Post post = getPost(id);
		post.addTag(tag);
		repository.save(post);
		return true;
	}
	
	@Override
	public boolean removeTag(String id, String tag) {
		if (tag == null) {
			throw new ForumBadRequestException();
		}
		Post post = getPost(id);
		if (post.getTags().remove(tag)) {
			repository.save(post);
			return true;
		}		
		return false;
	}

	@Override
	public Iterable<Post> findPostsByTags(List<String> tag) {
		return repository.findPostsByTagsIn(tag);
	}

	@Override
	public Iterable<Post> findPostsByAuthor(String author) {
		return repository.findPostsByAuthor(author);
	}

	@Override
	public Iterable<Post> findPostsByDates(DatePeriodDto datesDto) {		
		LocalDateTime from = LocalDateTime.of(datesDto.getFromDate(), LocalTime.MIN); 
		LocalDateTime to = LocalDateTime.of(datesDto.getToDate(), LocalTime.MIN);
		return repository.findPostsByDateCreatedBetween(from, to);
	}
}
