package com.security.demo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.demo.dto.RegistrationResultDTO;
import com.security.demo.dto.UserDTO;
import com.security.demo.model.Provider;
import com.security.demo.model.Role;
import com.security.demo.model.User;
import com.security.demo.service.RoleService;
import com.security.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@PostMapping("/process_register")
	@ResponseBody
	public ResponseEntity<RegistrationResultDTO> processRegistration(@RequestBody UserDTO user) {
		RegistrationResultDTO result = new RegistrationResultDTO();
		String email = user.getEmail();
		
		if(userService.findByEmail(email) != null) {
			result.setSuccess(false);
			result.setMessage("A user account with this email already exists.");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		
		String encodedPassword = encoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	    
	    Set<Role> roles = new HashSet<>();
    	roles.add(roleService.findByAuthority("ROLE_USER").orElseThrow());
	    
	    User registeredUser = new User(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), Provider.LOCAL, roles);
	    userService.save(registeredUser);
	    
	    result.setSuccess(true);
	    result.setMessage("Registration successful.");
	    
	    return ResponseEntity.ok().body(result);		
	}
}
