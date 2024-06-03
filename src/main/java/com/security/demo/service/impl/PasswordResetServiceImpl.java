package com.security.demo.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.security.demo.model.PasswordResetToken;
import com.security.demo.model.User;
import com.security.demo.repository.PasswordResetTokenRepository;
import com.security.demo.service.PasswordResetService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	private String generateToken() {
		return UUID.randomUUID().toString();
	}
	
	private Date calculateExpiryDate() {
		long currentTimeMillis = System.currentTimeMillis();
		return new Date(currentTimeMillis + PasswordResetToken.EXPIRATION);
	}
	
	@Override
	public PasswordResetToken createPasswordResetToken(User user) {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setToken(generateToken());
		passwordResetToken.setExpiryDate(calculateExpiryDate());
		passwordResetToken.setUser(user);
		return passwordResetTokenRepository.save(passwordResetToken);
	}

	@Override
	public boolean validatePasswordResetToken(String token) {
		Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);
		if (isTokenExpired(passwordResetToken.get())) {
			passwordResetTokenRepository.delete(passwordResetToken.get());
		}
		return passwordResetToken.isPresent() && !isTokenExpired(passwordResetToken.get());
	}

	private boolean isTokenExpired(PasswordResetToken token) {
		Date expiryDate = token.getExpiryDate();
		return expiryDate != null && expiryDate.before( new java.util.Date());
	}

	@Override
	public void sendResetEmail(String recipientEmail, String token) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("contact@shopme.com", "Cosmetics Support");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to reset your password";
		String link = "http://localhost:8181/reset_password?token=" + token;

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + link
				+ "\">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);
		javaMailSender.send(message);
	}

	@Override
	public Optional<PasswordResetToken> findByToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public Optional<PasswordResetToken> findByUser(User user) {
		return passwordResetTokenRepository.findByUser(user);
	}

	@Override
	public void deleteTokenBy(PasswordResetToken passwordResetToken) {
		passwordResetTokenRepository.delete(passwordResetToken);
		
	}

}
