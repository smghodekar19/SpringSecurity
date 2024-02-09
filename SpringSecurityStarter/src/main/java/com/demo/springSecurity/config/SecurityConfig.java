package com.demo.springSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf((csrf) -> csrf.disable())
				.authorizeHttpRequests((auth) -> 
				auth.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("USER")
				.requestMatchers(new AntPathRequestMatcher("/")).permitAll()
				.anyRequest().authenticated()
				)
				.httpBasic(Customizer.withDefaults())
				.build();
	}
	
	@Bean
	UserDetailsService users() {
		UserDetails user = User.builder().username("user").password(encoder().encode("password")).roles("USER").build();
		UserDetails admin = User.builder().username("admin").password(encoder().encode("password")).roles("ADMIN","USER").build();
		
		return new InMemoryUserDetailsManager(user, admin);
	}
	
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(10);
	}
	
//	@Bean
//	RoleHierarchy getHierarchy() {
//		RoleHierarchyImpl roles = new RoleHierarchyImpl();
//		roles.setHierarchy("ROLE_ADMIN > ROLE_USER");
//		return roles;
//		
//
//	}

}
