package com.shubhzz.enterprise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shubhzz.enterprise.entity.User;
import com.shubhzz.enterprise.repo.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	@Lazy
	PasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepo
				.findOneByEmail(username)
				.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
	}
	
	
	public String addUser(User user) throws Exception {
		if(userRepo.findOneByEmail(user.getEmail()).isPresent()) {
			throw new Exception("User with given email already exist, please login with same");
		}
		
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return "User Added Successfully";
	}

}
