package com.demo.springSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(
				(authz) -> authz
				.requestMatchers("/hello-admin/**").hasRole("admin")
				.requestMatchers("hello/**").hasRole("user")
				.anyRequest().authenticated())
		.httpBasic(Customizer.withDefaults()).build();
	}
	
	@Bean 
	public UserDetailsManager getUserDetails() {
		UserDetails admin = User.withUsername("Shubham")
				.password(bcryptPasswordEncoder().encode("admin")).roles("admin").build();
		UserDetails user = User.withUsername("User")
				.password(bcryptPasswordEncoder().encode("admin")).roles("user").build();
		return new InMemoryUserDetailsManager(admin,user);
		
	}
	
	@Bean
	public PasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean 
	public RoleHierarchy getRoleHirarchy() {
		RoleHierarchyImpl roles = new RoleHierarchyImpl();
		roles.setHierarchy("ROLE_admin > ROLE_user");
		return roles;
	}

}
