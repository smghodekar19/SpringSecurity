package com.shubhzz.enterprise.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.shubhzz.enterprise.filter.JwtFilter;
import com.shubhzz.enterprise.service.UserService;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http
			.csrf((csrf) -> csrf.disable())
			.headers((headers) -> headers.frameOptions((frame) -> frame.sameOrigin()))
			.authorizeHttpRequests((auth) -> auth
					.requestMatchers(
							new AntPathRequestMatcher	("/addNewUser"),
							new AntPathRequestMatcher	("/login"),
							new AntPathRequestMatcher	("/h2-console/**"),
							new AntPathRequestMatcher	("/")						
							)
					.permitAll()
					.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
//					.requestMatchers(new AntPathRequestMatcher("/user/**")).authenticated()
//					.requestMatchers(new AntPathRequestMatcher("/admin/**")).authenticated()
					.anyRequest().authenticated())
            .sessionManagement((session)  -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
	}
	
	@Bean 
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(getPasswordEncoder());
		return authProvider;
				
	}
	
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
		
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserService();
	}
}
