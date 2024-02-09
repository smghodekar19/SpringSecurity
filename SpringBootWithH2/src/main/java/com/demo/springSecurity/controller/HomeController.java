package com.demo.springSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public String welcomePage() {
		return "Welcome Home";
	}
	
	@GetMapping("/user")
	public String userPage() {
		return "Welcome User";
	}
	
	@GetMapping("/admin")
	public String adminPage() {
		return "Welcome Admin";
	}
}
