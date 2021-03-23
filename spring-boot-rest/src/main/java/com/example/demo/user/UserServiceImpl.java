package com.example.demo.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class UserServiceImpl implements UserService {

	private static List<User> users = new ArrayList<>();

	static {
		users.add(new User(1, "Deadpool"));
		users.add(new User(2, "Wolverine"));
	}

	@Override
	public List<User> getAllUsers() {
		return users;
	}

	@Override
	public List<User> saveuser(User user) {
		users.add(user);
		return users;
	}

	@Override
	public User findUserById(Integer id) {
		Optional<User> userOpt = users.stream().filter(i -> i.getId() == id).findFirst();
		
		return userOpt.isPresent() ? userOpt.get():null;
	}

}
