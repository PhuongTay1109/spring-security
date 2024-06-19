package com.security.demo.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.security.demo.model.Provider;
import com.security.demo.model.Role;
import com.security.demo.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class OAuth2CustomService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private Provider determineOAuth2Provider(Map<String, Object> attributes) {
        if (attributes.containsKey("sub")) {
            return Provider.GOOGLE;
        } else if (attributes.containsKey("id")) {
            return Provider.FACEBOOK;
        }
        return Provider.UNKNOWN;
    }

    public UserDetails loadUser(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
	
		
		String email = (String) attributes.get("email");
		User user = userService.findByEmail(email);
		
		// user with that email exist
		if (user != null) {
			
			return userService.loadUserByUsername(user.getEmail());
		}
		else {
			String fullName = (String) attributes.get("name");
			String firstName = fullName.split(" ")[0];
			String lastName = fullName.split(" ")[1];

			String password = "";
			
			Set<Role> roles = new HashSet<>();
			roles.add(roleService.findByAuthority("ROLE_USER").orElseThrow());
			
			Provider provider = determineOAuth2Provider(attributes); 
			
			User newUser = new User(email, password, firstName, lastName, provider, roles);
			userService.save(newUser);	
			
			return userService.loadUserByUsername(newUser.getEmail());
		}	
    }
}
