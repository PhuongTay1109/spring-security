package com.security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "authority")
	private String authority;

	@Override
	public String getAuthority() {
		return this.authority;
	}

	public Role(String authority) {
		super();
		this.authority = authority;
	}
}
