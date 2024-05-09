package com.security.demo.configuration;

import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.security.demo.service.impl.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsServiceImpl();
	}
	
	@Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService());
        daoProvider.setPasswordEncoder(bcryptPasswordEncoder);
        return new ProviderManager(Collections.singletonList(daoProvider));
    }
		
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/list_users").authenticated()
				.anyRequest().permitAll()
		 )
		.formLogin((form) -> form
				.loginPage("/login")
				.usernameParameter("email")
				.defaultSuccessUrl("/list_users")
				.permitAll()
		)
		.logout((logout) -> logout.permitAll());
		
		return http.build();
	}

}
