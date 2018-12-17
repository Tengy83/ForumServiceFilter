package telran.ashkelon2018.forum.service;

import java.util.List;

import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.dto.RoleDto;

public interface RoleService {
	Role addRole(RoleDto roleDto);
	Role editRole(String roleId, String newName);
	Role removeRole(String roleId);
	Role getRole(String roleId);
	List<Role> getAllRoles();
}
