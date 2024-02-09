package com.shubhzz.enterprise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shubhzz.enterprise.dto.AuthRequestDTO;
import com.shubhzz.enterprise.entity.User;
import com.shubhzz.enterprise.service.JwtService;
import com.shubhzz.enterprise.service.UserService;

@RestController
public class HomeController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JwtService jwtService;

	@GetMapping("/")
	public String userWelcomeMessage() {
		return "Hello User.... Good Morning !!";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String adminProfileMessage() {
		
		return "Hello Admin....Logged In with Admin Priviliges!!";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String userProfileMessage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		auth.getAuthorities().forEach(System.out::println);
		return "Hello User....Welcome to Profile Section!!";
	}
	
	
	@PostMapping("/addNewUser")
	public String addNewUser(@RequestBody User user) throws Exception {
		return userService.addUser(user);
	}
	
	@PostMapping("/login")
	public String loginAndGenerateToken(@RequestBody AuthRequestDTO authRequest) {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
				);
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		}
		else {
			throw new UsernameNotFoundException("Invalid Login Request");
		}
	}
}
