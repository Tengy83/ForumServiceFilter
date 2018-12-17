package telran.ashkelon2018.forum.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.dao.RoleRepository;
import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.dto.RoleDto;
import telran.ashkelon2018.forum.errors.ForumBadRequestException;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository repository;
	
	@Override
	public Role addRole(RoleDto roleDto) {
		Role role = new Role(roleDto.getRole().toLowerCase(), roleDto.getRole());
		repository.save(role);
		return role;
	}

	@Override
	public Role editRole(String roleId, String newName) {
		if (roleId.equals("user") || roleId.equals("admin") || roleId.equals("moderator")) {
			throw new ForumBadRequestException("The role of the User can not be renamed");
		}
		Role role = repository.findById(roleId).orElse(null);
		if (role == null) {
			throw new ForumBadRequestException("There is no such role");
		}
		role.setRole(newName);
		repository.save(role);
		return role;
	}

	@Override
	public Role removeRole(String roleId) {
		if (roleId.equals("user") || roleId.equals("admin") || roleId.equals("moderator")) {
			throw new ForumBadRequestException("The role of the User can not be deleted");
		}
		Role role = repository.findById(roleId).orElse(null);
		if (role == null) {
			throw new ForumBadRequestException("There is no such role");
		}
		repository.deleteById(roleId);
		return role;
	}

	@Override
	public Role getRole(String roleId) {
		Role role = repository.findById(roleId).orElse(null);
		if (role == null) {
			throw new ForumBadRequestException("There is no such role");
		}
		return role;
	}

	@Override
	public List<Role> getAllRoles() {		
		return repository.findAll();
	}

}
