package com.demo.springSecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "Welcome to homepage !!!";
	}
	
	@GetMapping("/user")
	public String userProfile() {
		return "User Profile Page ";
	}
	
	@GetMapping("/admin")
	public String adminProfile() {
		return "Admin Profile Page";
	}
	
	@GetMapping("/userDetails")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Object userDetails(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getPrincipal();
	}
}
