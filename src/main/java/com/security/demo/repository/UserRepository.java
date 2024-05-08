package com.security.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.security.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findAll();
	Optional<User> findById(Long id);
	
	@Query("SELECT u FROM User u WHERE u.email =?1")
	User findByEmail(String email);
}
