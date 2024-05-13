package com.security.demo;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.security.demo.model.Provider;
import com.security.demo.model.Role;
import com.security.demo.model.User;
import com.security.demo.service.impl.RoleServiceImpl;
import com.security.demo.service.impl.UserServiceImpl;

@SpringBootApplication
public class SpringSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(RoleServiceImpl roleService, UserServiceImpl userService, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			Role adminRole;
			Role userRole;
			if (roleService.findByAuthority("ROLE_ADMIN").isEmpty()) {
				adminRole = roleService.save(new Role("ROLE_ADMIN"));
				System.out.println(adminRole);
			}
			if (roleService.findByAuthority("ROLE_USER").isEmpty()) {
	            userRole = roleService.save(new Role("ROLE_USER"));
	            System.out.println(userRole);
	        }
			if (userService.findByEmail("admin@gmail.com") == null) {
	            Set<Role> roles = new HashSet<>();
	            roles.add(roleService.findByAuthority("ROLE_ADMIN").orElseThrow());
	            User admin = new User("admin@gmail.com", passwordEncoder.encode("admin1234@"),"admin", "1", Provider.LOCAL, roles);
	            userService.save(admin);
	        }
	        if (userService.findByEmail("user@gmail.com") == null) {
	        	Set<Role> roles = new HashSet<>();
	        	roles.add(roleService.findByAuthority("ROLE_USER").orElseThrow());
	        	User user = new User("user@gmail.com", passwordEncoder.encode("user1234@"),"user", "1", Provider.LOCAL, roles);
	        	userService.save(user);
	        }
		};
	}

}
