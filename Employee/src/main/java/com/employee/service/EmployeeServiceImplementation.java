package com.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.EmployeesNotFoundException;
import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;

@Service
public class EmployeeServiceImplementation implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public int addEmployee(Employee employee) {
		
		return employeeRepository.save(employee).getId();
		
		
	}

	@Override
	public List<Employee> getEmployees() throws EmployeesNotFoundException{
		
		List<Employee> employees=employeeRepository.findAll();
		
		if(employees==null||employees.isEmpty()) {
			throw new EmployeesNotFoundException("No Employees Found");
		}
		
		return employees;
		
	}

	@Override
	public int updateEmployee(Employee emp) throws EmployeeNotFoundException {
		
		Optional<Employee> employee=employeeRepository.findById(emp.getId());
		
		if(employee.isEmpty()) {
			throw new EmployeeNotFoundException("Employee with Id "+emp.getId()+" not found");
		}
		
		employeeRepository.save(emp);
		
		return emp.getId();
	}

	@Override
	public Employee deleteEmployee(int id) throws EmployeeNotFoundException {
		
		Optional<Employee> employee=employeeRepository.findById(id);
		
		if(employee==null||employee.isEmpty()) {
			throw new EmployeeNotFoundException("Employee with id "+id+" not found");
		}
		
		employeeRepository.deleteById(id);
		
		return employee.get();
	}

	@Override
	public Employee getEmployee(int id) throws EmployeeNotFoundException {
		
		Optional<Employee> employee=employeeRepository.findById(id);
		
		return employee.orElseThrow(()->new EmployeeNotFoundException("Employee with id "+id+" not found"));
		
	}
	
	
	
}
