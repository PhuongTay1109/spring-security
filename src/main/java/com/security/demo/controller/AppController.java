package com.security.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	
	@GetMapping("/login")
	String login() {
		return "/login";
	}	
	
	@GetMapping("/register") 
	public String showSignUpForm() {		
		return "/register";
	}
	
	@GetMapping("/list_users")
	public String viewUsersList(@AuthenticationPrincipal User userDetails, Model model) {
		List<User> listUsers = userService.findAll();
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("fullName", userDetails.getFullName());
		return "/users";
	}
}
