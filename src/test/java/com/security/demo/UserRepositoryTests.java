package com.security.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.security.demo.model.User;
import com.security.demo.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TestEntityManager entityManager;
	
//	@Test
//	public void testCreateUser() {
//		User user = new User();
//		user.setEmail("tuanbeo@gmail.com");
//		user.setPassword("tuanbeo110903");
//		user.setFirstName("Tuan");
//		user.setLastName("Beo");
//		
//		User savedUser = userRepo.save(user);
//		
//		User existUser = entityManager.find(User.class, savedUser.getId());
//		
//		assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
//	}
	
	@Test
	public void testFindUserByEmail() {
		String email="tuanbeo1@gmail.com";
		
		User user = userRepo.findByEmail(email);
		
		assertThat(user).isNotNull();
	}
	

}
