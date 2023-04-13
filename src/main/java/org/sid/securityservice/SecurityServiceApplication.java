package org.sid.securityservice;

import org.sid.securityservice.config.RSAKeyConfig;
import org.sid.securityservice.dto.UserForm;
import org.sid.securityservice.entity.AppRole;
import org.sid.securityservice.entity.AppUser;
import org.sid.securityservice.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyConfig.class)
public class SecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner startCommandLineRunner(AccountService accountService) {
		return args ->{
			AppRole roleAdmin = accountService.saveRole("ADMIN");
			AppRole roleUser = accountService.saveRole("USER");
			
			AppUser user = accountService.saveUser(new UserForm("user","user","user"));
			AppUser admin = accountService.saveUser(new UserForm("admin","admin","admin"));
			
			accountService.addRoleToUser(user.getUsername(), roleUser.getRolename());
			accountService.addRoleToUser(admin.getUsername(), roleUser.getRolename());
			accountService.addRoleToUser(admin.getUsername(), roleAdmin.getRolename());
		};
	}
	

}
