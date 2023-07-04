package com.jwtathentication.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtathentication.entity.User;
import com.jwtathentication.repository.UserRepository;
import com.jwtathentication.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;

	@Override
	public User findUserByName(String username) {

		return UserRepository.getUserByUserName(username);
	}

	@Override
	public User saveUser(User user) {
		System.out.println("user details save call........."+user.getFirstName());
		user.setRole("USER_ROLE");
		user.setPassword(PasswordEncoder.encode(user.getPassword()));
		return UserRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		System.out.println("get all user method call.....");
		return UserRepository.findAll();
	}
}
