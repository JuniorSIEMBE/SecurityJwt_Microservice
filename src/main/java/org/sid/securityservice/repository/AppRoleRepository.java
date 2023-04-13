package org.sid.securityservice.repository;

import org.sid.securityservice.entity.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long>{
	
	AppRole findByRolename(String rolename);

}
