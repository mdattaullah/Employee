package com.employee.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.employee.repository.EmployeeRepository;

@SpringBootTest
public class EmployeeServiceTest {
	
	@MockBean
	EmployeeRepository employeeRepository;

}
