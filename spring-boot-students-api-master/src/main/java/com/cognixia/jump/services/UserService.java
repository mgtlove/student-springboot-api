package com.cognixia.jump.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognixia.jump.exception.UserAlreadyExistsException;
import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.util.JwtUtil;

import io.jsonwebtoken.Jwts;
import jdk.jshell.spi.ExecutionControl.UserException;
import sun.jvm.hotspot.ui.FindByQueryPanel;

@Service
public class UserService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	JwtUtil tokenUtil;

	public boolean createNewUser(AuthenticationRequest registeringUser) throws Exception {
		Optional<User> isAlreadyRegistered = userRepository.findByUsername(registeringUser.getUsername());
		
		if(isAlreadyRegistered.isPresent()) {
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
	
	public String loginUser(AuthenticationRequest authenticationRequest) throws Exception {
		final String jwt;
		if (validateUserToken(authenticationRequest)) {
			final UserDetails userDetails = myUserDetailsService
					.loadUserByUsername(
							authenticationRequest.getUsername());
			
			jwt = tokenUtil.generateTokens(userDetails);
			User user = userRepository.findByUsername(authenticationRequest.getPassword()).get();
			user.setOnline(true);
			userRepository.save(user);
			return jwt;
		}

		return "login failed";
	}
	
	public boolean logoutUser (Authentication authentication) throws Exception {
		return invalidateUserToken(authentication);
	}
	
	private boolean validateUserToken(AuthenticationRequest authenticationRequest) throws Exception {
		boolean valid = false;
		try {
			
			if (userRepository.findByUsername(authenticationRequest.getUsername()).get().isOnline()) {
				throw new Exception("User is already logged in");
			}
			
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getUsername(), 
							authenticationRequest.getPassword())
					);
			

			valid = true;
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect Username or password", e);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return valid;	
	}
	
	private boolean invalidateUserToken(Authentication authentication) {

		Optional<User> userLoggingOut = userRepository.findByUsername(authentication.getName());
		
		if (userLoggingOut.isPresent()) {
			if (userLoggingOut.get().isOnline()) {
				userLoggingOut.get().setOnline(false);
				userRepository.save(userLoggingOut.get());
				return true;
			}
		}
		return false;
	}
	
}
