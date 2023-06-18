package com.employee.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.EmployeesNotFoundException;

@ControllerAdvice
public class EmployeeControllerAdvice{

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		
		Map<String,Object> responseBody=new HashMap<>();
		
		responseBody.put("Timestamp", Instant.now());
		responseBody.put("Status", HttpStatus.BAD_REQUEST);
		
		List<String> errors=new ArrayList<>();
		
		List<FieldError> fieldErrors=ex.getBindingResult().getFieldErrors();
		
		for(FieldError fieldError:fieldErrors) {
			errors.add(fieldError.getDefaultMessage());
		}
		
		responseBody.put("Message", errors);
		
		return new ResponseEntity<Object>(responseBody,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){
		
		Map<String,Object> response=new HashMap<>();
		
		response.put("Timestamp", Instant.now());
		response.put("Status", HttpStatus.BAD_REQUEST);
		response.put("Message", "Employee details is missing");
		
		return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(EmployeesNotFoundException.class)
	public ResponseEntity<Object> handleEmployeesNotFound(EmployeesNotFoundException ex){
		
		Map<String,Object> response=new HashMap<>();
		
		response.put("Timestamp", Instant.now());
		response.put("Status",HttpStatus.NOT_FOUND);
		response.put("Message",ex.getMessage());
		
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<Object> handleEmployeeNotFound(EmployeeNotFoundException exception){
		Map<String,Object> response=new HashMap<>();
		response.put("Timestamp", Instant.now());
		response.put("Status", HttpStatus.NOT_FOUND);
		response.put("Message", exception.getMessage());
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleArgumentMisMatch(MethodArgumentTypeMismatchException exception){
		Map<String,Object> response=new LinkedHashMap<>();
		
		response.put("Timestamp", Instant.now());
		response.put("Status", HttpStatus.BAD_REQUEST);
		response.put("Message", "Path parameter should be integer");
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleRequestParameter(MissingServletRequestParameterException exception){
		
		Map<String,Object> response=new LinkedHashMap<>();
		
		response.put("Timestamp", Instant.now());
		response.put("Status", HttpStatus.BAD_REQUEST);
		response.put("Message", "Please provide Employee Id");
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
}
