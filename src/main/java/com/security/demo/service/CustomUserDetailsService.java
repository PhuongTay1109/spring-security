package com.security.demo.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.security.demo.model.CustomUserDetails;

public interface CustomUserDetailsService extends UserDetailsService {

	@Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
