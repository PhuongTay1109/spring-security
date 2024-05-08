package com.security.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.security.demo.model.CustomUserDetails;
import com.security.demo.model.User;
import com.security.demo.repository.UserRepository;

public class CustomUserDetailsServiceImpl implements com.security.demo.service.CustomUserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);
		if (user == null)
			throw new UsernameNotFoundException("User not found");
		return new CustomUserDetails(user);
	}

}
