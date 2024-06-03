package com.security.demo.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import com.security.demo.model.PasswordResetToken;
import com.security.demo.model.User;

import jakarta.mail.MessagingException;

public interface PasswordResetService {
	public void deleteTokenBy(PasswordResetToken passwordResetToken);
	public Optional<PasswordResetToken> findByToken(String token);
	public Optional<PasswordResetToken> findByUser(User user);
	public PasswordResetToken createPasswordResetToken(User user);
	public boolean validatePasswordResetToken(String token);
	public void sendResetEmail(String recipientEmail, String token) throws UnsupportedEncodingException, MessagingException;
}
