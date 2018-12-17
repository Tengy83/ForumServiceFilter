package telran.ashkelon2018.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.dto.DatePeriodDto;
import telran.ashkelon2018.forum.dto.NewCommentDto;
import telran.ashkelon2018.forum.dto.NewPostDto;
import telran.ashkelon2018.forum.service.ForumService;

@RestController
@RequestMapping("/forum")
public class ForumController {
	
	@Autowired
	ForumService forumService;
	
	@PostMapping("/post")
	public Post addNewPost(@RequestBody NewPostDto newPost, @RequestHeader("Authorization") String token) {
		return forumService.addNewPost(newPost, token);
	}
	
	@GetMapping("/post/{id}")
	public Post getPost(@PathVariable String id, @RequestHeader("Authorization") String token) {
		return forumService.getPost(id);
	}
	
	@DeleteMapping("/post/{id}")
	public Post removePost(@PathVariable String id, @RequestHeader("Authorization") String token) {
		return forumService.removePost(id);
	}
	
	@PutMapping("/post/{id}")
	public Post updatePost(@PathVariable String id, @RequestBody NewPostDto newPost, @RequestHeader("Authorization") String token) {
		return forumService.updatePost(id, newPost);
	}
	
	@PostMapping("/post/{id}/like")
	public boolean addLike(@PathVariable String id, @RequestHeader("Authorization") String token) {
		return forumService.addLike(id, token);
	}
	
	@PostMapping("/post/{id}/comment")
	public Post addComment(@PathVariable String id, @RequestBody NewCommentDto newCommentDto, @RequestHeader("Authorization") String token) {
		return forumService.addComment(id, newCommentDto, token);
	}
	
	@PostMapping("/posts/tags")
	public Iterable<Post> findPostsByTags(@RequestBody List<String> tags){
		return forumService.findPostsByTags(tags);
	}
	
	@GetMapping("posts/author/{author}")
	public Iterable<Post> finfPostsByAuthor(@PathVariable String author){
		return forumService.findPostsByAuthor(author);
	}
	
	@PostMapping("posts")
	public Iterable<Post> findPostsByDates(@RequestBody DatePeriodDto datesDto){
		return forumService.findPostsByDates(datesDto);
	}
	

	@PostMapping("post/{id}/tag/{tag}")
	public boolean addTag(@PathVariable String id, @PathVariable String tag, @RequestHeader("Authorization") String token) {
		return forumService.addTag(id, tag);
	}
	
	@DeleteMapping("post/{id}/tag/{tag}")
	public boolean removeTag(@PathVariable String id, @PathVariable String tag, @RequestHeader("Authorization") String token) {
		return forumService.removeTag(id, tag);
	}

	@PostMapping("post/{idPost}/comment/{idComment}/like")
	public boolean addLikeComment(@PathVariable String idPost, @PathVariable String idComment, @RequestHeader("Authorization") String token) {
		return forumService.addLikeComment(idPost, idComment, token);
	}
	
	@PutMapping("post/{idPost}/comment/{idComment}")
	public Post updateComment(@PathVariable String idPost, @PathVariable String idComment, @RequestBody NewCommentDto newCommentDto , @RequestHeader("Authorization") String token) {
		return forumService.updareComment(idPost, idComment, newCommentDto);
	}

	@DeleteMapping("post/{idPost}/comment/{idComment}")
	public Post removeComment(@PathVariable String idPost, @PathVariable String idComment, @RequestHeader("Authorization") String token) {
		return forumService.removeComment(idPost, idComment);
	}
}
