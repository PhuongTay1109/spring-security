package com.security.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.security.demo.model.PasswordResetToken;
import com.security.demo.model.Provider;
import com.security.demo.model.User;
import com.security.demo.service.impl.PasswordResetServiceImpl;
import com.security.demo.service.impl.UserServiceImpl;

import jakarta.mail.MessagingException;


@Controller
public class AppController {
	@Autowired
	private UserServiceImpl userService;	
	
	@Autowired
	PasswordResetServiceImpl passwordResetTokenService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
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
	
	@GetMapping("/forgot_password")
	private String getForgotPassword() {
		return "/forgot_password";
	}
	
	@PostMapping("/forgot_password")
	public String processForgotPassword(@RequestParam Map<String, String> body) throws UnsupportedEncodingException, MessagingException {
		
		String email = body.get("email");
		User user = userService.findByEmail(email);
		
		if (user == null) {
			return "redirect:/forgot_password?error";
		}
		
		Provider provider = user.getProvider();
		if (!provider.equals(Provider.LOCAL)) {
			return "redirect:/forgot_password?oauth2";
		}
		
		// có token từ lần quên trước thì xóa rồi gửi token mới
		Optional<PasswordResetToken> existingToken = passwordResetTokenService.findByUser(user);
		if (existingToken.isPresent()) {
			passwordResetTokenService.deleteTokenBy(existingToken.get());
		}
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
		passwordResetTokenService.sendResetEmail(email, passwordResetToken.getToken());
		
		return "redirect:/forgot_password?success";
	}
	
	@GetMapping("/reset_password")
	public String getResetPassword(@RequestParam Map<String, String> body, Model model ) {
		
		//After PUT /reset_password, it redirect to GET /reset_password?success
		String success = body.get("success");
		if (success != null) {			
			return "/reset_password";
		}
		
	 	String token = body.get("token");
		if (!passwordResetTokenService.validatePasswordResetToken(token)) {			
	 		return "redirect:/forgot_password?invalid_token";
	 	}
		
	 	PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token).get();
	 	User user = passwordResetToken.getUser();
	 	model.addAttribute("userId", user.getUserId());
	 	
		return "/reset_password";
	}
	
	@ResponseBody
	@PutMapping("/reset_password")
	public Map<String, Object> processResetPassword(@RequestBody Map<String, String> body) {
		return userService.resetPassword(body);
	}
}
