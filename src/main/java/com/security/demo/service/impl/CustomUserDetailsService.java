package com.security.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.security.demo.model.CustomUserDetails;
import com.security.demo.repository.UserRepository;

public class CustomUserDetailsService implements com.security.demo.service.CustomUserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
