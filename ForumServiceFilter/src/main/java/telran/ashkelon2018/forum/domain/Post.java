package telran.ashkelon2018.forum.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString
@Document(collection = "forum_filter")
public class Post {
	private String id;
	private @Setter String title;
	private @Setter String content;
	private String author;
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dateCreated;
	private @Setter Set<String> tags;
	private int likes;
	private Set<String> whoPutLikesPost;
	private Set<Comment> comments;
	
	// Constructor
	
	public Post(String title, String content, String author, Set<String> tags) {
		super();
		this.title = title;
		this.content = content;
		this.author = author;
		this.tags = tags;
		dateCreated = LocalDateTime.now();
		comments = new TreeSet<>((c1,c2)->c1.getDateCreated().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli() > c2.getDateCreated().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()? 1 : -1);
		whoPutLikesPost = new HashSet<>();
	}
	
	// Methods
	
	public boolean addLike(String whoPutLikePost) {
		if (whoPutLikesPost.add(whoPutLikePost)) {
			likes = whoPutLikesPost.size();
			return true;
		}		
		return false;
	}
	
	public boolean addComment(Comment comment) {
		return comments.add(comment);
	}
	
	public boolean addTag(String tag) {
		return tags.add(tag);
	}
	
	public boolean removeTag(String tag) {
		return tags.remove(tag);
	}
	public void removeAllTags() {
		tags.clear();
	}
}
