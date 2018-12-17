package telran.ashkelon2018.forum.service;

import java.util.List;

import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.dto.DatePeriodDto;
import telran.ashkelon2018.forum.dto.NewCommentDto;
import telran.ashkelon2018.forum.dto.NewPostDto;

public interface ForumService {
	Post addNewPost(NewPostDto newPost, String token);
	Post getPost(String id);
	Post removePost(String id);
	Post updatePost(String id, NewPostDto newPost);
	boolean addTag(String id, String tag);
	boolean removeTag(String id, String tag);
	boolean addLike(String id, String token);
	boolean addLikeComment(String idPost, String idComment, String token);
	Post addComment(String id, NewCommentDto newCommentDto, String token);
	Post updareComment(String idPost, String idComment, NewCommentDto newCommentDto);
	Post removeComment(String idPost, String idComment);
	Iterable<Post> findPostsByTags(List<String> tags);
	Iterable<Post> findPostsByAuthor(String author);
	Iterable<Post> findPostsByDates(DatePeriodDto datesDto);
}
