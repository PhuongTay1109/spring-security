package com.security.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.demo.model.User;
import com.security.demo.repository.UserRepository;
import com.security.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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

	@Override
	public Map<String, Object> resetPassword(Map<String, String> body) {
		Map<String, Object> response = new HashMap<>();

		// validate
		boolean isValid = true;
		boolean isNewPasswordValid = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")
				.matcher(body.get("newPassword")).matches();

		if (body.get("newPassword").equals(body.get("currentPassword"))) {
			response.put("newPassword", "New password must be different from current password!");
			isValid = false;
		} else if (!isNewPasswordValid) {
			response.put("newPassword",
					"New password must contain at least 6 characters, including letters and numbers!");
			isValid = false;
		}

		if (!body.get("confirmPassword").equals(body.get("newPassword"))) {
			response.put("confirmPassword", "Confirm password must match new password!");
			isValid = false;
		}

		response.put("isValid", isValid);

		if (isValid) {
			Integer id = Integer.parseInt(body.get("userId"));
			User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
			String encodedPassword = passwordEncoder.encode(body.get("confirmPassword"));
			existingUser.setPassword(encodedPassword);
			userRepository.save(existingUser);
		}

		return response;
	}
}
