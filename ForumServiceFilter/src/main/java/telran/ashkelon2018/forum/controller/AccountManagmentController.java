package telran.ashkelon2018.forum.controller;

import java.util.List;
import java.util.Set;

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

import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.dto.RoleDto;
import telran.ashkelon2018.forum.dto.UserProfileDTO;
import telran.ashkelon2018.forum.dto.UserRegDto;
import telran.ashkelon2018.forum.service.AccountService;
import telran.ashkelon2018.forum.service.RoleService;

@RestController
@RequestMapping("/account")
public class AccountManagmentController {
	@Autowired
	AccountService accountService;
	
	@Autowired
	RoleService roleService;
	
	@PostMapping
	public UserProfileDTO register(@RequestBody UserRegDto userRegDto, @RequestHeader("Authorization") String token) {
		return accountService.addUser(userRegDto, token);
	}
	
	@PutMapping
	public UserProfileDTO update(@RequestBody UserRegDto userRegDto, @RequestHeader("Authorization") String token) {
		return accountService.editUser(userRegDto, token);
	}
	
	@GetMapping
	public UserProfileDTO getUser(@RequestHeader("Authorization") String token) {
		return accountService.getUser(token);
	}
	
	@DeleteMapping("/{login}")
	public UserProfileDTO remove(@PathVariable String login, @RequestHeader("Authorization") String token) {
		return accountService.removeUser(login, token);
	}
	
	@PutMapping("/{login}/{role}")
	public Set<RoleDto> addRole(@PathVariable String login, @PathVariable String role, @RequestHeader("Authorization") String token){
		return accountService.addRole(login, role, token);
	}
	
	@DeleteMapping("/{login}/{role}")
	public Set<RoleDto> removeRole(@PathVariable String login, @PathVariable String role, @RequestHeader("Authorization") String token){
		return accountService.removeRole(login, role, token);
	}
	
	@PutMapping("/password")	
	public void changePassword( @RequestHeader("X-Authorization") String password, @RequestHeader("Authorization") String token) {
		accountService.changePassword(password, token);
	}
	
	@PostMapping("/role")
	public Role addRole(@RequestBody RoleDto roleDto, @RequestHeader("Authorization") String token) {
		return roleService.addRole(roleDto);
	}
	
	@PutMapping("/role/{roleId}/{newName}")
	public Role editRole(@PathVariable String roleId, @PathVariable String newName, @RequestHeader("Authorization") String token) {
		return roleService.editRole(roleId, newName);
	}
	
	@DeleteMapping("/role/{role}")
	public Role removeRole(@PathVariable String role, @RequestHeader("Authorization") String token) {
		return roleService.removeRole(role);
	}
	
	@GetMapping("/role/{role}")
	public Role getRole(@PathVariable String role, @RequestHeader("Authorization") String token) {
		return roleService.getRole(role);
	}
	
	@GetMapping("/roles")
	List<Role> getAllRoles(@RequestHeader("Authorization") String token){
		return roleService.getAllRoles();
	}
}
