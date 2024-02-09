package com.demo.springSecurity.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String getWelcomeMessage() {
		return "Hello, Good Morning !!";
	}
	@GetMapping("/hello-admin")
	public String getWelcomeMessageAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user = (UserDetails) auth.getPrincipal();
		return "Hello, Good Morning !! ..... " + user.getUsername() +" Signed UP";
	}
	
	@GetMapping("/retrieve-user")
	public Principal getPrincipal(Principal principal) {
		return principal;
	}
}
