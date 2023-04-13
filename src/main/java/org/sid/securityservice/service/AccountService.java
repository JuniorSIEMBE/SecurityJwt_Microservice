package org.sid.securityservice.service;

import java.util.Collection;

import org.sid.securityservice.dto.UserForm;
import org.sid.securityservice.entity.AppRole;
import org.sid.securityservice.entity.AppUser;

public interface AccountService {
	
	public AppUser saveUser(UserForm user);
	
	public AppRole saveRole(String rolename);
	
	public AppUser loadUserByUsername(String username);
	
	public void addRoleToUser(String username, String rolename);
	
	public Collection<AppUser> findAllUsers();
	
	public Collection<AppRole> findAllRole();

}
