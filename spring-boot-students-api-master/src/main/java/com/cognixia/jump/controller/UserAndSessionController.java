package com.cognixia.jump.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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

import com.cognixia.jump.exception.LogoutFailedException;
import com.cognixia.jump.exception.ResourceNotFoundException;

import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;
import com.cognixia.jump.model.GeoIP;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.UserDevice;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.services.GeoIPService;
import com.cognixia.jump.services.MyUserDetailsService;
import com.cognixia.jump.services.UserDeviceService;
import com.cognixia.jump.services.UserService;
import com.cognixia.jump.util.JwtUtil;
import com.maxmind.geoip2.exception.GeoIp2Exception;

@RestController
@RequestMapping("/api")
public class UserAndSessionController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	JwtUtil jwtTokenUtil;
	
	@Autowired
	GeoIPService geoIPService;
	
	@Autowired
	UserDeviceService userDeviceService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
			HttpServletRequest req) 
			throws Exception {
		final String jwtLogin = userService.loginUser(authenticationRequest, req);

		return ResponseEntity.ok(jwtLogin);
		
	}
	
	@RequestMapping(value = "/logout" , method = RequestMethod.GET)
	public ResponseEntity<?> invalidateAuthenticationToken (Authentication authentication) throws Exception {
		
		if (!userService.logoutUser(authentication)) {
			throw new LogoutFailedException("Could not logout.");
		}
		
		return ResponseEntity.ok("Logged out.");
	}
	
	// Get the current User info
	@GetMapping("/user/current")
	public ResponseEntity<?> getLoggedInUser(HttpServletRequest req) throws  Exception {
		String jwt = req.getHeader("Authorization").split(" ")[1];
		String username = jwtTokenUtil.extractUsername(jwt);
		UserDevice userDevice = userDeviceService.getUserDeviceDetails(req);

		Map<String, ?> userInfo = new TreeMap<>();
		
		userInfo = Map.of(
				"JWT" , jwt,
				"Username", username,
				"User Device Details", userDevice
				);	
		
		return ResponseEntity.ok(userInfo);
		
		// Take in the Authentication from the Request, you need to change req to Datatype "Authentication".
//		Object user = auth.getPrincipal(); 
//		return ResponseEntity.ok(user);
	}
	
	// Create a new User
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<?> createNewUser(@RequestBody AuthenticationRequest user ) throws Exception {
		
			userService.createNewUser(user);
			
			return ResponseEntity.ok(user.getUsername() + " has been created.");

	}
	
	// Show list of all users - should only be for ROLE_ADMINs
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
