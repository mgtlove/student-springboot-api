package com.cognixia.jump.util;

import org.springframework.stereotype.Service;

import com.cognixia.jump.model.UserDevice;
import com.cognixia.jump.services.UserDeviceService;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// will create new jwts
// pull up info from existing jwts
@Service
public class JwtUtil {
	
	@Autowired
	UserDeviceService userDeviceService;
	
	// used with algorithm to hash/encode our token
	private final String SECRET_KEY = "jump";
	
	// get the username for this token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	
	// get expiration date for this token
	public Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	// takes a token and a claims resolver to find out what the claims are for that particular token
	// so find that data that was passed in through the token and be able to access it again (username, expiration date)
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver ) {
		
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	
	private Claims extractAllClaims(String token) {
		
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	
	// checks if the token has expired yet by checking the current date & time and comparing it to the expiration
	private Boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}
	
	
	// returns those generated tokens after a successful authentication
	public String generateTokens(UserDetails userDetails, HttpServletRequest req) throws Exception {
		
		// claims info/data you want to include in payload of token besides the user info
		Map<String, Object> claims = new HashMap<>();
		claims.put("Device-Details", userDeviceService.getUserDeviceDetails(req));
		
		// returns token for user given along with any claims
		return createToken(claims, userDetails.getUsername());
	}
	
	// creates the token
	private String createToken(Map<String, Object> claims, String subject) {
		
		// sets claims
		// subject (person that is being authenticated)
		// set when the token was issued
		// set expiration when token expires and can be no longer used (here its set for 10 hrs)
		// sign it with particular algorithm and secret key that lets you know this token is authentic
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt( new Date( System.currentTimeMillis() ) )
				.setIssuer("JUMP-Student-API")
				//.claim("Device-Details", claims.get("Device-Details"))
				.setExpiration( new Date( System.currentTimeMillis() + 1000 * 60 * 60 * 10 ) )
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	// will validate the token and check if the current token is for the right user requesting it and that the token isn't expired
	public Boolean validateToken(String token, UserDetails userDetails, HttpServletRequest req) throws Exception {
		
		Gson gson = new Gson();
		
		
		final String username = extractUsername(token);
		
		final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		
		
		final String userDevice = claims.get("Device-Details").toString().trim();
		System.out.println("Token device origin: " + userDevice);
		System.out.println("Request device origin: " + userDeviceService.getUserDeviceDetails(req).toJson());
		System.out.println("Device match: " + userDevice.equals(userDeviceService.getUserDeviceDetails(req).toJson()));
		
		//final String userDevice = claims.get("Device Details").toString();
		return ( username.equals( userDetails.getUsername() ) && 
				!isTokenExpired(token) &&
				userDevice.equals(userDeviceService.getUserDeviceDetails(req).toJson())
				);
	}
}
