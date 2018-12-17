package telran.ashkelon2018;

import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.ashkelon2018.forum.dao.RoleRepository;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.domain.UserAccount;

@SpringBootApplication
public class ForumServiceFilterApplication implements CommandLineRunner {
	@Autowired
	UserAccountRepository userAccountRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(ForumServiceFilterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userAccountRepository.existsById("admin")) {
			String hashPassword = BCrypt.hashpw("admin", BCrypt.gensalt());
			if (!userAccountRepository.existsById("admin")) {
				
				roleRepository.save(new Role("admin", "admin"));
				roleRepository.save(new Role("moderator", "Moderator"));
				roleRepository.save(new Role("user", "User"));
			
				UserAccount userAccount = UserAccount.builder()
								.login("admin")
								.password(hashPassword)
								.firstName("Super")
								.lastName("Admin")
								.expdate(LocalDateTime.now().plusYears(25))
								.role(roleRepository.findById("admin").get())
								.build();
				userAccountRepository.save(userAccount);
			}
		}
		
	}

}

