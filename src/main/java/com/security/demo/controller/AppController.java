package com.security.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.demo.model.CustomUserDetails;
import com.security.demo.model.User;
import com.security.demo.service.impl.UserServiceImpl;


@Controller
public class AppController {
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
    private BCryptPasswordEncoder encoder;
	
	
	@GetMapping("/")
	public String viewHomePage() {
		return "/home";
	}
	
	@GetMapping("/register") 
	public String showSignUpForm(Model model) {		
		model.addAttribute("user", new User());
		
		return "/signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegistration(User user) {
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		userService.save(user);
		
		return "/register_success";
		
	}
	
	@GetMapping("/list_users")
	public String viewUsersList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		List<User> listUsers = userService.findAll();
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("fullName", userDetails.getFullName());
		return "/users";
	}
	

}
