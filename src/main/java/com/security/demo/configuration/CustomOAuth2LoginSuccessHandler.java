package com.security.demo.configuration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.security.demo.model.CustomOAuth2User;
import com.security.demo.model.Provider;
import com.security.demo.model.Role;
import com.security.demo.model.User;
import com.security.demo.service.RoleService;
import com.security.demo.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	public CustomOAuth2LoginSuccessHandler(UserService userService) {
		this.userService = userService;
	}
	
    private Provider determineOAuth2Provider(String clientName) {
    	switch (clientName.toUpperCase()) {
		case "FACEBOOK":
			return Provider.FACEBOOK;
		case "GOOGLE":
			return Provider.GOOGLE;
		default:
			return Provider.LOCAL;
		}
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

		String email = oauthUser.getEmail();
		String name = oauthUser.getName();
		String clientName = oauthUser.getOauth2ClientName();

		// Map the client name to the Provider enum
		Provider provider = determineOAuth2Provider(clientName);
		
		// Check if the user already exists in the database
		User existingUser = userService.findByEmail(email);

		if (existingUser == null) {
			String firstName = name.split(" ")[0];
			String lastName = name.split(" ")[1];

			// Create a new user and save it to the database
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);
			newUser.setProvider(provider);
			Set<Role> roles = new HashSet<>();
			roles.add(roleService.findByAuthority("ROLE_USER").orElseThrow());
			userService.save(newUser);
			
			// Set authentication in the security context
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(newUser,
					null, newUser.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		else {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					existingUser, null, existingUser.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}

		response.sendRedirect("/list_users");
	}
}
