package com.example.demo.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity
public class Employee {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String role;

	public Employee(String name, String role) {
		super();
		this.name = name;
		this.role = role;
	}

	public Employee() {
	}

}
