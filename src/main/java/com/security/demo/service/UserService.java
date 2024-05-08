package com.security.demo.service;

import java.util.List;

import com.security.demo.model.User;

public interface UserService {

	public void save(User user);
	public List<User> findAll();
	public User findByEmail(String email);
	public User findById(Long id);
	public void deleteById(Long id);
}
