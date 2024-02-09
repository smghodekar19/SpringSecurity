package com.shubhzz.enterprise.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubhzz.enterprise.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
	
	Optional<User> findOneByEmail(String email);

}
