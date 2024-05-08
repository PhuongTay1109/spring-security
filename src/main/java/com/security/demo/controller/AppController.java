package com.security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.demo.model.User;
import com.security.demo.service.impl.UserServiceImpl;


@Controller
public class AppController {
	@Autowired
	private UserServiceImpl userService;
	
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
		userService.save(user);
		return "/register_success";
		
	}
	

}
