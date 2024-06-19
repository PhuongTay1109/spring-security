package com.security.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.demo.model.Role;
import com.security.demo.repository.RoleRepository;
import com.security.demo.service.RoleService;

@Service
public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;

	public Optional<Role> findByAuthority(String authority) {		
		return roleRepository.findByAuthority(authority);
	}

	public Role save(Role role) {		
		return roleRepository.save(role);
	}

}
