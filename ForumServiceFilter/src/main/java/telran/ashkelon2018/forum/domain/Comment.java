package telran.ashkelon2018.forum.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

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
public class Comment {
	private @Setter String id;
	private String user;
    private @Setter String message;
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCreated;
    private int likes;
    private Set<String> whoPutLikesComment;
    
    // Constructor
    
	public Comment(String user, String message) {
		super();
		this.user = user;
		this.message = message;
		dateCreated = LocalDateTime.now();
		id = user + dateCreated.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		whoPutLikesComment = new HashSet<>();
	}
	
	// Methods
	
	public boolean addLike(String whoPutLikeComment) {
		if (whoPutLikesComment.add(whoPutLikeComment)) {
			likes++;
			return true;
		}
		return  false;
	}
}
