# Book 1 : Master java webservices and restful api with spring boot

# SECTION 1: RESTFUL WEBSERVICES WITH SPRING AND SPRING BOOT

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

its always good to write your custom error message.
> :red_circle: **@ControllerAdvice** has to be added to the custom exception class.
lets go through the get user id service and see

```java
\\inside UserServiceImpl class
@Override
	public User findUserById(Integer id) {
		Optional<User> userOpt = users.stream().filter(i -> i.getId() == id).findFirst();
		
		return userOpt.isPresent() ? userOpt.get():null;
	}

\\ create UserNotFound class
public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4553824199994173031L;

	private String message;

	public UserNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

\\ create the controller advice class and name it as ExceptionHelper class
package com.example.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHelper extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> userNotFoundException(UserNotFoundException unfe) {
		logger.error("USER NOT FOUND: " +unfe.getMessage());
		return new ResponseEntity<Object>(unfe.getMessage(), HttpStatus.NOT_FOUND);
	}

}


\\now in the controller 

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
		User user = userService.findUserById(id);
		if(user == null) {
			throw new UserNotFoundException("user with id "+id+" does not exist .");
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
 // check this in postman
 http://localhost:8080/test/users/9
 
 this will give you 404 not found with a valid message and in the log 
 you will see below message
 
 ERROR 23336 --- [nio-8080-exec-1] com.example.demo.user.ExceptionHelper    : USER NOT FOUND: user with id 9 does not exist .


```
## 11. Implementing swagger documentation 

there are few tags that we need to have idea on .

![project structure](images/Capture15.JPG)

1. **Info Tag**: this will give you the highlevel view about the API

![project structure](images/Capture16.JPG)

2. **tags**: tags are used to group resources in to multiple catagories

3. **path**: includes the details of all the resources we are exposing .

![project structure](images/Capture17.JPG)

4. **definations**: these are basically the objects .

![project structure](images/Capture18.JPG)

:ledger: lets create basic swagger documentation

1. create a new class and name it as SwaggerConfig
2. add @Configuration and @EnableSwagger2 to it .

```java
<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-boot-starter</artifactId>
			<version>3.0.0</version>
		</dependency>
==============================================================
package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2);
	}

}

//for api doc
http://localhost:8080/v2/api-docs

//for swagger ui
http://localhost:8080/swagger-ui/index.html#/

```

## 12 . Implementing static filtering with RESTFUL services

lets say there is one field which we do not really want to show in the response ; then in that case we can use the static filtering .

basically use **@JsonIgnore** on the field .

```java
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private Integer id;
	private String name;
	@JsonIgnore
	private int age;

}

```

lets say there are too many fields that we want to ignore ; in that case we can use the **JsonIgnoreProperties** annotation in the class level with the field names .


basically use "JsonIgnoreProperties" for static filtering .


```java
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "age", "salary", "password" })
public class User {
	private Integer id;
	private String name;
	private int age;
	private double salary;
	private String password;

}

```
## 13 . Implementing dynamic filtering with RESTFUL services

if we want to disable one field based on the result of a condition then we can use dynamic filtering .

```java
package com.example.demo.user;

import com.fasterxml.jackson.annotation.JsonFilter;

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
@JsonFilter("userFil")
public class User {
	private Integer id;
	private String name;
	private int age;
	private double salary;
	private String password;

}

//////////////////////////////////////////////////////////

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
	
```


# Book 2 : Spring security : from zero to hero (udemy: https://www.udemy.com/course/spring-security-zero-to-master)

## section 1: Getting started 

lets create a simple web application and add spring security dependancy to it .

```java
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-test</artifactId>
	<scope>test</scope>
</dependency>
		
// controller

package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome to spring security";
	}

}

// now when you run the app it will ask for the username and the password
username: user
Password: displayed on console

//if you want to display custom generated details then mention this in the application.properties file

spring.security.user.name=bijan
spring.security.user.password=test

```
### 1.1 : Understanding how multiple request works without spring credentials

when you run the **http://localhost:8080/welcome** in browser it will first direct you to the **http://localhost:8080/login** page . here you can put your credentials and it will take you to the browser page . and once you are there , if you will try to refresh the page then you will notice that it does not take you back to the login page anymore . thats because it has stored the password as a cookie . we can see it better in the postman .

![project structure](images/Capture19.JPG)

![project structure](images/Capture20.JPG)

![project structure](images/Capture21.JPG)

![project structure](images/Capture22.JPG)


## Section 2: Changing the default security configuration

### Section 2.1: Creating backend services for the application
this is what we are expected to build ...

![project structure](images/Capture23.JPG)

so basically we need to build those controllers . Lets build those 6 controllers in a package .

![project structure](images/Capture24.JPG)

### Section 2.2: Checking the default configuration inside the spring security library

in this section we will create the configuration class and extend the configure method . In the future videos we will customize the overridden method to ask us for passwords in few scenarios .

![project structure](images/Capture25.JPG)
![project structure](images/Capture26.JPG)

### Section 2.3: Modifying the code as per our custom requirements 

>http.authorizeRequests((requests) -> requests.anyRequest().authenticated());

if you notice in the code above ; it allows any request thats coming in to be authenticated .so in this section we will try to let it authenticate only for few specific request and permir for other services using the **"antMatchers()"** method .

in this case we will disable the authorization and try to test the /contact service and see if it works or not in postman .

```java
package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfigurator extends WebSecurityConfigurerAdapter {
	/**
	 * myAccount -secured
	 * myBalance - secured
	 * myLoans - secured
	 * myCards - secured
	 * 
	 * others not secured
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests((requests) -> requests.antMatchers("/myAccount").authenticated()
				.antMatchers("/mybalance").authenticated()
				.antMatchers("/myLoans").authenticated()
				.antMatchers("/myCards").authenticated()
				
				//no need for security 
				
				.antMatchers("/contact").permitAll()
				.antMatchers("/notices").permitAll());
		http.formLogin();
		http.httpBasic();
	}

}


```

![project structure](images/Capture27.JPG)


### Section 2.4: Denying all the requests

lets say we want to deny all requests eventhough they have all reqired authentications or not .

> http.authorizeRequests(req->req.anyRequest().denyAll());

```java

package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfigurator extends WebSecurityConfigurerAdapter {
	/**
	 * myAccount -secured
	 * myBalance - secured
	 * myLoans - secured
	 * myCards - secured
	 * 
	 * others not secured
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//make every request to be authenticated
		
		/*
		 * http.authorizeRequests(req->req.anyRequest().authenticated());
		 * 
		 */
		
		//accept few with authentication and for others no need to authenticate 
		// just allow them .
		
		/*
		 * http.authorizeRequests((requests) ->
		 * requests.antMatchers("/myAccount").authenticated()
		 * .antMatchers("/mybalance").authenticated()
		 * .antMatchers("/myLoans").authenticated()
		 * .antMatchers("/myCards").authenticated() .antMatchers("/contact").permitAll()
		 * .antMatchers("/notices").permitAll());
		 */
		
		// to deny all the request 
		
		http.authorizeRequests(req->req.anyRequest().denyAll());
		
		http.formLogin();
		http.httpBasic();
	}

}


```

now if you will test the contact service with the basic authentication; you will get a forbidden error .

![project structure](images/Capture28.JPG)

### Section 2.5: Permit all the requests

its pretty straight-forward 

> http.authorizeRequests(req->req.anyRequest().permitAll());

now in postman if you make the authorisation as empty then also it will work .
![project structure](images/Capture29.JPG)


## Section 3: Defining and managing users 

![project structure](images/Capture30.JPG)

### 3.1 : Configuring users using inMemoryAuthentication

in this section we will learn how to configure for multiple user authentication and how to use password encoder .

it will help us in preventing to mention passwords in the application.properties .

for this we will override the **configure(AuthenticationManagerBuilder auth)** method .

```java
@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("admin").and()
		.withUser("test").password("test").authorities("test")
		.and()
		.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}
	

```

so basically now if we put the application.properties user name and password in the browser , it won't work .But if we put **"admin"** as username and **"admin"** as password then ideally it will work .


![project structure](images/Capture31.JPG)

![project structure](images/Capture32.JPG)


### 3.2 : Configuring users using inMemoryUserDetailsManager

in this case instead of using inMemoryAuthentication we will use InMemoryUserDetailsManager

```java
@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
		UserDetails userDetails1 = User.withUsername("developer").password("dev").authorities("development").build();
		UserDetails userDetails2 = User.withUsername("tester").password("test").authorities("test").build();
		inMemoryUserDetailsManager.createUser(userDetails1);
		inMemoryUserDetailsManager.createUser(userDetails2);
		auth.userDetailsService(inMemoryUserDetailsManager);
	}
	
	@Bean
	public PasswordEncoder pawd() {
		return NoOpPasswordEncoder.getInstance();
	}
	

```
### 3.3 : Understanding userManagement interfaces and classes .

![project structure](images/Capture33.JPG)

### 3.4 : Deep dive in to UserDetails interface .

### 3.5 : Deep dive in to UserDetailsService interface .

### 3.6 : Done so far and the next thing to do .

so far , as per the diagram above we have completed the InMemoryUserDetailsManager class . 

lets try to use the JdbcUserDetailsManager class .

in this case we will use the H2 in memory database . if we look at the code above , the Jdbc... class requires you to have a table named User with columns as "username" "password" and "enabled" . and somewhat the same for the "authorities" .

so lets create a sql file and store below mentioned details there .

![project structure](images/Capture34.JPG)

```sql
create table users(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(200) not null,
	enabled boolean not null
);

create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);

```

now lets put some data inside it as well 

```sql
insert into users (username, password, enabled) values ('bob', '{noop}123', true);
insert into authorities (username, authority) values ('bob', 'ROLE_USER');

insert into users (username, password, enabled) values ('sara', '{noop}234', true);
insert into authorities (username, authority) values ('sara', 'ROLE_ADMIN');

```

now in the application.properties file we need to enable the h2 database .

use it as a task .

## Section 4 : Password management with password encoders .

### 4.1 Deep dive in to password encoder

![project structure](images/Capture35.JPG)

**1. NoOpPasswordEncoder** :

          1st of all its deprecated . so basically spring tells us not to use it . 
**2. StandardPasswordEncoder** :

      its better than the first one but still nothing to remember . its also deprecated to some extent .
      
**3. BCryptPasswordEncoder** :

        this is the most commonly used password encoder as it used "hashing" . 

**4. SCryptPasswordEncoder** :

        it has more added security features . recommended but still BCrypt is the most commonly used .


