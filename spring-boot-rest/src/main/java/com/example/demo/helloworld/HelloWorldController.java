package com.example.demo.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	@GetMapping("/hello-world")
	public String show() {
		return "hello world service";
	}
	
	@GetMapping("/hello-world-bean")
	public HelloWorldBean showBean() {
		return new HelloWorldBean("hello world service");
	}
	
	@GetMapping("/hello-world-bean/{name}")
	public HelloWorldBean showBean(@PathVariable String name) {
		return new HelloWorldBean(name);
	}

}
