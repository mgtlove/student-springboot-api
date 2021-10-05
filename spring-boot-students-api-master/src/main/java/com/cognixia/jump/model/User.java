package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public enum Role {
		ROLE_USER, ROLE_ADMIN
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String username;

	@Column(nullable = false)
	private String password;
	
	@Column(columnDefinition = "boolean default true")
	private boolean enabled;
	
	@Column(columnDefinition = "boolean default false")
	private boolean online;
	
	@ManyToMany
	@JoinTable(
			name = "user_userDevice",
			joinColumns = @JoinColumn(name = "userDevice_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	Set<UserDevice> userDevices;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	public User() {
		this(-1L, "N/A", "N/A", false, false, null, Role.ROLE_USER);
	}
	
	public User(Long id, String username, String password, boolean enabled, boolean online, Set<UserDevice> userDevices,
			Role role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.online = online;
		this.userDevices = userDevices;
		this.role = role;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Set<UserDevice> getUserDevices() {
		return userDevices;
	}

	public void setUserDevices(Set<UserDevice> userDevices) {
		this.userDevices = userDevices;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled
				+ ", online=" + online + ", userDevices=" + userDevices + ", role=" + role + "]";
	}
	
}
