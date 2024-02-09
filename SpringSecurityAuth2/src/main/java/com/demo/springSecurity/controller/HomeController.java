package com.demo.springSecurity.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "Welcome Home !!";
	}
	
	@GetMapping("/user")	
	public String userHome(Principal principal) {
		System.out.println("Principal: " + principal.toString());
		
		if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
			Map<String, Object> userAttributes = authToken.getPrincipal().getAttributes();
			String userName = (String) userAttributes.getOrDefault("name", "DEFAULT_USERNAME");
			String userEmail = (String) userAttributes.getOrDefault("email", "DEFAULT_USERNAME");
			String gitHubLink = (String)userAttributes.getOrDefault("url", "DEFAULT_USER_EMAIL");
			
			return String.format("User Signed UP Email:%s Name:%s URL:%s", userName, userEmail, gitHubLink);
		}
		return "Welcome User !!!";
	}
}
