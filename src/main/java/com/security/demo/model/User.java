package com.security.demo.model;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Provider provider;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles_junction",
			joinColumns = {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="role_id")}
	)
	Set<Role> authorities;
	
	public User(String email, String password, String firstName, String lastName, Provider provider,
			Set<Role> authorities) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.provider = provider;
		this.authorities = authorities;
	}	
	
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
