package com.example.demo.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

	// get list of all users
	List<User> getAllUsers();

	/*
	 * save the new user to the list and get the list of all users (it should
	 * include the new user too)
	 */
	List<User> saveuser(User user);

	// get user based on id search
	User findUserById(Integer id);

}
