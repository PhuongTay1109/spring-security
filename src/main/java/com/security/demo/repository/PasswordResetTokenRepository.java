package com.security.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.demo.model.PasswordResetToken;
import com.security.demo.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	Optional<PasswordResetToken> findByToken(String token);
	Optional<PasswordResetToken> findByUser(User user);
}
