package com.cognixia.jump.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognixia.jump.exception.UserAlreadyExistsException;
import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public boolean createNewUser(AuthenticationRequest registeringUser) throws Exception {
		User isAlreadyRegistered = userRepository.findByUsername(registeringUser.getUsername());
		
		if(isAlreadyRegistered != null) {
			throw new UserAlreadyExistsException(registeringUser.getUsername());
		}
		
		User newUser = new User();
		newUser.setUsername(registeringUser.getUsername());
		newUser.setPassword(passwordEncoder.encode(registeringUser.getPassword()));
		newUser.setEnabled(true);
		newUser.setRole(Role.valueOf("ROLE_USER"));
		
		userRepository.save(newUser);
		return true;
		
	}
	
}
