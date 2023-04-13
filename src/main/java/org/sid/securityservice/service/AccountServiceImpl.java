package org.sid.securityservice.service;

import java.util.Collection;

import org.sid.securityservice.dto.UserForm;
import org.sid.securityservice.entity.AppRole;
import org.sid.securityservice.entity.AppUser;
import org.sid.securityservice.repository.AppRoleRepository;
import org.sid.securityservice.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	
	private AppUserRepository appUserRepository;
	
	private AppRoleRepository appRoleRepository;
	
	private PasswordEncoder passwordEncoder;

	AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AppUser saveUser(UserForm user) {
		if(user.getPassword().equals(user.getConfirmPassword())) {
			AppUser appUser = new AppUser();
			appUser.setUsername(user.getUsername());
			appUser.setPassword(passwordEncoder.encode(user.getPassword()));
			return appUserRepository.save(appUser);
		}else {
			//return an exception
		}
		return null;
	}

	@Override
	public AppRole saveRole(String rolename) {
		return appRoleRepository.save(new AppRole(null, rolename));
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		AppUser user = appUserRepository.findByUsername(username);
		AppRole role = appRoleRepository.findByRolename(rolename);
		user.getRoles().add(role);
	}

	@Override
	public Collection<AppUser> findAllUsers() {
		return appUserRepository.findAll();
	}

	@Override
	public Collection<AppRole> findAllRole() {
		return appRoleRepository.findAll();
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		return appUserRepository.findByUsername(username);
	}

}
