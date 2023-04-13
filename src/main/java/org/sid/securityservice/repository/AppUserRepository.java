package org.sid.securityservice.repository;

import org.sid.securityservice.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{
	
	AppUser findByUsername(String username);

}
