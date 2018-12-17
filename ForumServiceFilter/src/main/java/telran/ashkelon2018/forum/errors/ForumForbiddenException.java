package telran.ashkelon2018.forum.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForumForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ForumForbiddenException(String message) {
		super(message);
	}
	
	

}
