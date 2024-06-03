package com.security.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.security.demo.model.User;

public interface UserService extends UserDetailsService {

	public void save(User user);
	public List<User> findAll();
	public User findByEmail(String email);
	public User findById(Integer id);
	public void deleteById(Integer id);
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
	
	public Map<String, Object> resetPassword(Map<String, String> body);
}
