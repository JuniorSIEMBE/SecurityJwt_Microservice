package org.sid.securityservice.service;

import org.sid.securityservice.entity.AppUser;
import org.sid.securityservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	private AppUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser Appuser = userRepository.findByUsername(username);
		if(Appuser==null) throw new UsernameNotFoundException(username);
		UserPrincipal user = new UserPrincipal(Appuser);

		return user;
	}

}
