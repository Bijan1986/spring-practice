# Book 1 : Master java webservices and restful api with spring boot
## 1. initializing restful webservices with spring boot
import features from spring starter

![features to import](images/img1.JPG)

the project structure will look like this<br>

![project structure](images/img2.JPG)

## 2. understanding the restful services we will create

```
create a user : POST /users
delete a user: DELETE /users/{1}
get all user : GET /users
get a user by id : GET /users/{1}

retrieve all posts for a user : GET /users/{id}/posts
create posts for a user : POST /users/{id}/posts
retrieve details of a post : GET /users/{id}/posts/{post_id}

```

## 3. creating a hello world services

![project structure](images/img3.JPG)

now in the local host

![project structure](images/img4.JPG)


## 4. enhancing the hello world services to return a bean/object

![project structure](images/Capture5.JPG)
![project structure](images/Capture6.JPG)
![project structure](images/Capture7.JPG)

## 5. enhancing the hello world service with a path variable

![project structure](images/Capture8.JPG)
![project structure](images/Capture9.JPG)

## 6. creating user bean and user services

here we are creating a User bean . <br>
The user bean will have the following
```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private Integer id;
	private String name;

}

```
now we will create the **UserService** *interface* and annotate with **@Service** <br>
```java
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


```
then we can create the Service implemantation class and make it implement the UserService interface .

```java

package com.example.demo.user;

import java.util.ArrayList;
import java.util.List;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findUserById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}

```

now we will create the Controller class and add field injection to the UserService like 
> :red_circle: **@Autowired <br>
                 private UserService**

```java
package com.example.demo.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	

}

```

now as you can see in the above controller the Request mapping has the path as "test" and the @GetMapping has the path as "/users" so the uri will become **"http://localhost:8080/test/users"** now run this in postman

![project structure](images/Capture10.JPG)

## 7. calling with the path variable

in this example we will try to get a user with a specific user id <br>
we already have the abstract method defined in the Service class **"User findUserById(Integer id);"** we just need to add body for it in the implementation class . <br>

> :red_circle: **@PathVariable** to be added to the method .

```java
//inside UserServiceImpl class
@Override
	public User findUserById(Integer id) {
		return users.stream().filter(i-> i.getId()==id).findFirst().get();
	}
// inside UserController class

@GetMapping("/users/{id}")
	public User getUserById(@PathVariable("id") Integer id) {
		return userService.findUserById(id);
	}

```

![project structure](images/Capture11.JPG)

## 8. implementing the post method
Always remember, a successful post message should give you **201 : created** status message .<br>
but for now lets do the 200 way and in the next slide we will check the response entity and make it to 201

1. the service implementation class 
```java
@Override
	public List<User> saveuser(User user) {
		users.add(user);
		return users;
	}

```

2. the controller class
```java
@PostMapping("/users")
	public List<User> saveUser(@RequestBody User user) {
		return userService.saveuser(user);
	}
```

3. postman
>make sure to send the body as mentioned below
```
{
    "id": 3,
    "name": "Shazam"
}
```
![project structure](images/Capture12.JPG)


## 9. Enhancing post method to return 201 and the uri location

to get 201 we need to return responseEntity on the return type of the post method instead of the list <br>

```java
URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(user.getId())
				.toUri();
```

and the response entity should look like this

```java
 ResponseEntity.status(HttpStatus.CREATED)
				.header("user header", uri.toString())
				.body(userService.saveuser(user));
```
so the method will be as follows

```java
@PostMapping("/users")
public ResponseEntity<List<User>>saveUser(@RequestBody User user) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(user.getId())
				.toUri();
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.header("user header", uri.toString())
				.body(userService.saveuser(user));
	}
```
now if we will check the postman 
![project structure](images/Capture13.JPG)
and the header will look like this
![project structure](images/Capture14.JPG)

## 10. Implementing exception handling
