package com.example.demo.employee.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.demo.employee.Employee;

@Repository
@Transactional
public class EmployeeDaoService {

	@PersistenceContext
	private EntityManager entityManager;

	public long insert(Employee emp) {
		// open transaction
		entityManager.persist(emp);

		// close transaction
		return emp.getId();
	}
}
