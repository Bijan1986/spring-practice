package com.example.demo.user;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
@RequestMapping("/test")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<MappingJacksonValue> getUserById(@PathVariable("id") Integer id) {
		User user = userService.findUserById(id);
		if (user == null) {
			throw new UserNotFoundException("user with id " + id + " does not exist .");
		}
		SimpleBeanPropertyFilter userBeanFilter = SimpleBeanPropertyFilter.filterOutAllExcept("salary","password");
		FilterProvider userFilter = new SimpleFilterProvider().addFilter("userFil", userBeanFilter);
		MappingJacksonValue mapping = new MappingJacksonValue(user);
		mapping.setFilters(userFilter);
		return ResponseEntity.status(HttpStatus.OK).body(mapping);
	}

	@PostMapping("/users")
	public ResponseEntity<List<User>> saveUser(@RequestBody User user) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

		return ResponseEntity.status(HttpStatus.CREATED).header("user header", uri.toString())
				.body(userService.saveuser(user));
	}

}
