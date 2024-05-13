package com.security.demo.service;

import java.util.Optional;

import com.security.demo.model.Role;

public interface RoleService {
	public Optional<Role> findByAuthority(String authority);
	
	public Role save(Role role);
}
