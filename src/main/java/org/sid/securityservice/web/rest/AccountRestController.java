package org.sid.securityservice.web.rest;

import java.util.Collection;
import java.util.Map;

import org.sid.securityservice.dto.UserForm;
import org.sid.securityservice.dto.UserRoleForm;
import org.sid.securityservice.entity.AppRole;
import org.sid.securityservice.entity.AppUser;
import org.sid.securityservice.service.AccountService;
import org.sid.securityservice.service.JwtManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@Tag(name = "Account", description = "the Account API")
@AllArgsConstructor
public class AccountRestController {
	
	private AccountService accountService;
	private JwtManager jwtManager;

    @Operation(summary = "Add user", description = "Add a new user", tags = { "user" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "User created",
                content = @Content(schema = @Schema(implementation = AppUser.class))), 
        @ApiResponse(responseCode = "400", description = "Invalid input"), 
        @ApiResponse(responseCode = "409", description = "Contact already exists") })
	@PostMapping(path = "/users")
	public AppUser saveUser(@Parameter(description="User to add. Cannot null or empty.", 
            required=true, schema= @Schema(implementation = Contact.class)) @RequestBody UserForm form) {
		return accountService.saveUser(form);
	}
	
    @Operation(summary = "Add role", description = "Add a new role", tags = { "role" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "role created",
                content = @Content(schema = @Schema(implementation = AppUser.class))), 
        @ApiResponse(responseCode = "400", description = "Invalid input"), 
        @ApiResponse(responseCode = "409", description = "Contact already exists") })
	@PostMapping(path = "/roles")
	public AppRole saveRole(@Parameter(description="Role to add. Cannot null or empty.", 
            required=true, schema= @Schema(implementation = String.class)) @RequestBody String rolename) {
		return accountService.saveRole(rolename);
	}
	
    @Operation(summary = "Add user", description = "Add a new user", tags = { "user" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "User created",
                content = @Content(schema = @Schema(implementation = AppUser.class))), 
        @ApiResponse(responseCode = "400", description = "Invalid input"), 
        @ApiResponse(responseCode = "409", description = "Contact already exists") })
	@PostMapping(path = "/userTorole")
	public void addRoleToRole(UserRoleForm form) {
		accountService.addRoleToUser(form.getUsername(), form.getRolename());
	}
	
	@Operation(summary = "find users", description = "Find all user", tags = { "users" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful", content = 
    @Content(array = @ArraySchema(schema = @Schema(implementation = AppUser.class))))})
	@GetMapping(path = "/users")
	public Collection<AppUser> getUsers(){
		return accountService.findAllUsers();
	}
	
	@Operation(summary = "find role", description = "find all roles", tags = { "roles" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful", content = 
    @Content(array = @ArraySchema(schema = @Schema(implementation = AppRole.class))))})
	@GetMapping(path = "/roles")
	public Collection<AppRole> getRoles(){
		return accountService.findAllRole();
	}

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginForm loginForm){
    	return jwtManager.getToken(loginForm.getUsername(), loginForm.getPassword(), loginForm.isWithRefreshToken(), loginForm.getGrantType(), loginForm.getRefreshToken());
    }
}
