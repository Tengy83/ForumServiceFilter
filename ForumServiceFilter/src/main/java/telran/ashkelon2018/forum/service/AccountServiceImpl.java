package telran.ashkelon2018.forum.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredentials;
import telran.ashkelon2018.forum.dao.RoleRepository;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.domain.UserAccount;
import telran.ashkelon2018.forum.dto.RoleDto;
import telran.ashkelon2018.forum.dto.UserProfileDTO;
import telran.ashkelon2018.forum.dto.UserRegDto;
import telran.ashkelon2018.forum.errors.ForumBadRequestException;
import telran.ashkelon2018.forum.errors.UserConflictExeption;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	UserAccountRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	AccountConfiguration accountConfiguration;
	
	@Override
	public UserProfileDTO addUser(UserRegDto userRegDto, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		if (userRepository.existsById(credentials.getLogin())) {
			throw new UserConflictExeption();
		}
		String hashPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder()
										.login(credentials.getLogin())
										.password(hashPassword)
										.firstName(userRegDto.getFirstName())
										.lastName(userRegDto.getLastName())
										.role(roleRepository.findById("user").get())
										.expdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
										.build();
		userRepository.save(userAccount);
		return convertToUserProfileDTO(userAccount);
	}
	
	private UserProfileDTO convertToUserProfileDTO(UserAccount userAccount) {
		
		return UserProfileDTO.builder()
				.firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName())
				.login(userAccount.getLogin())
				.roles(userAccount.getRoles().stream().map(r-> convertToRoleDto(r)).collect(Collectors.toSet()))
				.build();
	}
	
	private RoleDto convertToRoleDto(Role role) {
		return RoleDto.builder()
					.role(role.getRole())
					.build();
	}

	@Override
	public UserProfileDTO editUser(UserRegDto userRegDto, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		if (userRegDto.getFirstName() != null) {
			userAccount.setFirstName(userRegDto.getFirstName());
		}
		if (userRegDto.getLastName() != null) {
			userAccount.setLastName(userRegDto.getLastName());
		}
		userRepository.save(userAccount);
		return convertToUserProfileDTO(userAccount);
	}

	@Override
	public UserProfileDTO removeUser(String login, String token) {
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		if (userAccount != null) {
			userRepository.delete(userAccount);
		} else {
			throw new ForumBadRequestException("This user does not exist");
		}
		return convertToUserProfileDTO(userAccount);
	}

	@Override
	public Set<RoleDto> addRole(String login, String role, String token) {
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		Role roleObj = roleRepository.findById(role.toLowerCase()).orElse(null);
		if (userAccount != null && roleObj != null) {
			userAccount.addRole(roleObj);
			userRepository.save(userAccount);
		} else {
			throw new ForumBadRequestException("You cannot add a role");
		}
		return userAccount.getRoles().stream()
					.map(r->convertToRoleDto(r))
					.collect(Collectors.toSet());
	}

	@Override
	public Set<RoleDto> removeRole(String login, String role, String token) {
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		Role roleObj = roleRepository.findById(role).orElse(null);
		if (userAccount != null && roleObj != null && !roleObj.getRoleId().equals("user")) {
			userAccount.removeRole(roleObj);
			userRepository.save(userAccount);
		}else {
			throw new ForumBadRequestException("You cannot remove a role");
		}	
		return userAccount.getRoles().stream()
				.map(r->convertToRoleDto(r))
				.collect(Collectors.toSet());
	}

	@Override
	public void changePassword(String password, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		userAccount.setPassword(hashPassword);
		userAccount.setExpdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()));
		userRepository.save(userAccount);
	}

	@Override
	public UserProfileDTO getUser(String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		return convertToUserProfileDTO(userAccount);
	}

	@Override
	public UserAccount getAuthor(String login) {
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		if (userAccount == null) {
			throw new ForumBadRequestException("This user does not exist");
		}
		return userAccount;
	}

}
