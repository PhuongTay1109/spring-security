package com.security.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.demo.model.User;
import com.security.demo.repository.UserRepository;
import com.security.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired 
	UserRepository userRepository;
	
	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
	        return null;
	    }
		return user.get();
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public User findByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (!user.isPresent()) {
	        return null;
	    }
		return user.get();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(email);
		if (!user.isPresent()) {
	        throw new UsernameNotFoundException("User not found with email: " + email);
	    }
		return user.get();
	}

}
