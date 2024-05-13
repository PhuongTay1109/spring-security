package com.security.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.demo.model.Role;
import com.security.demo.repository.RoleRepository;
import com.security.demo.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public Optional<Role> findByAuthority(String authority) {		
		return roleRepository.findByAuthority(authority);
	}

	@Override
	public Role save(Role role) {		
		return roleRepository.save(role);
	}

}
