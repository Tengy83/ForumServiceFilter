package telran.ashkelon2018.forum.service;

import java.util.Set;

import telran.ashkelon2018.forum.domain.UserAccount;
import telran.ashkelon2018.forum.dto.RoleDto;
import telran.ashkelon2018.forum.dto.UserProfileDTO;
import telran.ashkelon2018.forum.dto.UserRegDto;

public interface AccountService {
	UserProfileDTO addUser(UserRegDto userRegDto,String token);
	UserProfileDTO editUser(UserRegDto userRegDto,String token);
	UserProfileDTO removeUser(String login,String token);
	UserProfileDTO getUser(String login);
	UserAccount getAuthor(String login);
	Set<RoleDto> addRole(String login, String role, String token);
	Set<RoleDto> removeRole(String login, String role, String token);
	void changePassword(String password, String token);
}
