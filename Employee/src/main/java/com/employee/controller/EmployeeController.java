package com.employee.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.EmployeesNotFoundException;
import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import com.employee.service.EmployeeServiceImplementation;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/add")
	public ResponseEntity<Object> addEmployee(@Valid @RequestBody Employee emp) {
		
		int empId=employeeService.addEmployee(emp);
		
		Map<String,Object> response=new HashMap<>();
		
		response.put("Timestamp", Instant.now());
		response.put("Status", HttpStatus.OK);
		response.put("Message", "Employee with id "+empId+" successfully saved");
		
		return new ResponseEntity<>(response ,HttpStatus.OK);
		
	}
	
	@GetMapping("/getAllEmployee")
	public ResponseEntity<Object> getEmployees() throws EmployeesNotFoundException{
		
		Map<String,Object> response=new HashMap<>();
		
		response.put("Timestamp", Instant.now());
		response.put("Status", HttpStatus.OK);
		response.put("Message",employeeService.getEmployees());
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<Object> updateEmployee(@Valid @RequestBody Employee employee) throws EmployeeNotFoundException{
		
		Map<String,Object> response=new HashMap<>();
		
		response.put("Timestamp", Instant.now());
		response.put("Status", HttpStatus.OK);
		response.put("Message", "Employee with id "+employeeService.updateEmployee(employee)+" is updated successfully");
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteEmployee(@PathVariable("id") int id) throws EmployeeNotFoundException{
		Map<String,Object> response=new LinkedHashMap<>();
		response.put("Timestamp",Instant.now());
		response.put("Status", HttpStatus.OK);
		response.put("Message", "Deleted Employee: "+employeeService.deleteEmployee(id));
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	@GetMapping("/getEmployee")
	public ResponseEntity<Object> getEmployee(@RequestParam int id) throws EmployeeNotFoundException{
		
		Map<String,Object> response=new LinkedHashMap<>();
		response.put("Timestamp",Instant.now());
		response.put("Status", HttpStatus.OK);
		response.put("Message", employeeService.getEmployee(id));
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
}
