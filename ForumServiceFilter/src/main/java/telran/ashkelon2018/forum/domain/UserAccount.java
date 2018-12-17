package telran.ashkelon2018.forum.domain;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of= {"login"})
@ToString
@Document(collection="forum_user")
public class UserAccount {
	@Id
	private String login;
	private String password;
	private String firstName;
	private String lastName;
	@Singular
	private Set<Role> roles;
	private LocalDateTime expdate;
	
	// Methods
	
	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);		
	}
}
