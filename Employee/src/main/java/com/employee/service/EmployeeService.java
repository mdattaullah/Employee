package com.employee.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.EmployeesNotFoundException;
import com.employee.model.Employee;


public interface EmployeeService {
	
	public int addEmployee(Employee emp);
	
	public int updateEmployee(Employee emp) throws EmployeeNotFoundException;
	
	public List<Employee> getEmployees() throws EmployeesNotFoundException;
	
	public Employee deleteEmployee(int id) throws EmployeeNotFoundException;
	
	public Employee getEmployee(int id) throws EmployeeNotFoundException;

}
