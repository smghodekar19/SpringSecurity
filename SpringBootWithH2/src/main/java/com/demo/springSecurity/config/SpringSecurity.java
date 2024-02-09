package com.demo.springSecurity.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration	
@EnableWebSecurity
public class SpringSecurity {

	@Bean
	 SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf((csrf) -> csrf.disable())
		.headers((headers) -> headers.frameOptions((frame) -> frame.sameOrigin()))
		.authorizeHttpRequests((auth) -> 
			auth
			.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/admin/**")).authenticated()
			.requestMatchers(new AntPathRequestMatcher("/user/**")).authenticated()
			.requestMatchers("/").permitAll()
			.requestMatchers("/error").permitAll()
			.anyRequest().authenticated()
			)
		.formLogin(Customizer.withDefaults());
		return http.build();
	}
	
	
	@Bean
	DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
			.build();
	}
	
	@Bean
	UserDetailsService users(DataSource source, PasswordEncoder encoder) {
		UserDetails user = User.builder().username("user").password(encoder.encode("password")).roles("USER").build();
		UserDetails admin = User.builder().username("admin").password(encoder.encode("password")).roles("ADMIN").build();
		JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(source);
		userManager.deleteUser("user");
		userManager.deleteUser("admin");
		userManager.createUser(user);
		userManager.createUser(admin);
		return userManager;
	}
	
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(12);
	}
}
//create table users(
//username varchar_ignorecase(50) not null primary key,
//password varchar_ignorecase(500) not null,
//enabled boolean not null
//);
//
//create table authorities (
//username varchar_ignorecase(50) not null,
//authority varchar_ignorecase(50) not null,
//constraint fk_authorities_users foreign key(username) references users(username)
//);
//create unique index ix_auth_username on authorities (username,authority);
