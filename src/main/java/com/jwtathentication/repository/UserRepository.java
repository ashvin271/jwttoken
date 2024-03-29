package com.jwtathentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jwtathentication.entity.User;


@Repository
public interface UserRepository  extends JpaRepository<User, Long>{

	@Query("select u from User u where u.username=?1")
	public User getUserByUserName(String email);
}
