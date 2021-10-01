package com.cognixia.jump.controller;


import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;

import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;

import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.services.MyUserDetailsService;
import com.cognixia.jump.services.UserService;
import com.cognixia.jump.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class UserAndSessionController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) 
			throws Exception {
		final String jwtLogin = userService.loginUser(authenticationRequest);
		if (jwtLogin.equals("login failed")) {
			return new ResponseEntity<>(jwtLogin, HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(jwtLogin);
		
	}
	
	@RequestMapping(value = "/logout" , method = RequestMethod.GET)
	public ResponseEntity<?> invalidateAuthenticationToken (Authentication authentication) throws Exception {
		;
		
		return ResponseEntity.ok(userService.logoutUser(authentication));
	}
	
	// Get the current User info
	@GetMapping("/user/current")
	public ResponseEntity<?> getLoggedInUser(Authentication auth) {
		
		// Take in HTTP Servlet Request Object
//		String jwt = req.getHeader("Authorization").split(" ")[1];
//		String value = jwtTokenUtil.extractUsername(jwt);
//		return ResponseEntity.ok(value);
		
		// Take in the Authentication from the Request, you need to change req to Datatype "Authentication".
		Object user = auth.getPrincipal(); 
		
		return ResponseEntity.ok(user);
	}
	
	// Create a new User
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<?> createNewUser(@RequestBody AuthenticationRequest user ) throws Exception {
		
		try {
			userService.createNewUser(user);
			
			return ResponseEntity.ok(user.getUsername() + " has been created.");
		} catch (Exception e) {
			throw e;
		}

	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsers() throws Exception {
		
		return ResponseEntity.ok(userRepository.findAll());
		
	}
	
	@DeleteMapping("/delete/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable long id) throws Exception {
		
		Optional<User> found = userRepository.findById(id);
		
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("User with id: " + String.valueOf(id));
		}
		
		userRepository.deleteById(id);
		return ResponseEntity.ok("User with id: " + id + " has been deleted.");
			
	}
	

	
}
