package com.security.demo.configuration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.security.demo.model.Provider;
import com.security.demo.model.Role;
import com.security.demo.model.User;
import com.security.demo.service.impl.RoleServiceImpl;
import com.security.demo.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	UserServiceImpl userService;
	
	@Autowired
	RoleServiceImpl roleService;
	
	private Provider determineOAuth2Provider(Map<String, Object> attributes) {
	    if (attributes.containsKey("sub")) {
	        return Provider.GOOGLE;
	    } else if (attributes.containsKey("id")) {
	        return Provider.FACEBOOK;
	    }
	    return Provider.UNKNOWN;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		
		System.out.println(attributes);
		
		String email = (String) attributes.get("email");
		User user = userService.findByEmail(email);
		
		// user with that email exist
		if (user != null) {
			UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
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
			
			UserDetails userDetails = userService.loadUserByUsername(newUser.getEmail());
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		response.sendRedirect("/list_users");
	}

}
