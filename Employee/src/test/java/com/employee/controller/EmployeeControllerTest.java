package com.employee.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.error.ShouldHaveSameSizeAs;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.employee.EmployeeApplication;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response;

@SpringBootTest()
@ExtendWith(SpringExtension.class)
//@ContextConfiguration()
@AutoConfigureMockMvc
public class EmployeeControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	private final ObjectMapper objectMapper=new ObjectMapper();
	
	@Test
	@WithMockUser(authorities = {"USER"})
	public void testGetAllEmployee() throws Exception{
		
		Mockito.when(employeeService.getEmployees()).thenReturn(List.of(new Employee(1,"A","C","A@abc.com")));
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/employee/getAllEmployee").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.Message",hasSize(1)))
		.andExpect(jsonPath("$.Message[0].firstName",is("A")));
		
		Mockito.when(employeeService.getEmployees()).thenReturn(List.of());
		requestBuilder=MockMvcRequestBuilders.get("/employee/getAllEmployee").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.Message",hasSize(0)));

	}
	
	@Test
	@WithMockUser(authorities= {"USER"})
	public void testGetEmployee() throws Exception {
		
		Mockito.when(employeeService.getEmployee(1)).thenReturn(new Employee(1,"A","C","A@abc.com"));
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/employee/getEmployee?id=1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
		.andExpect(jsonPath("$.Message.firstName", is("A")))
		.andExpect(jsonPath("$.Message.id", is(1)))
		.andExpect(jsonPath("$.Message.lastName",is("C")))
		.andExpect(jsonPath("$.Message.email", is("A@abc.com")));
		
		Mockito.when(employeeService.getEmployee(2)).thenThrow(new EmployeeNotFoundException("Employee with id 2 not found"));
		requestBuilder=MockMvcRequestBuilders.get("/employee/getEmployee?id=2").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder)
		.andExpect(result->assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
		.andExpect(result->assertEquals("Employee with id 2 not found",result.getResolvedException().getMessage()))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.Message", is("Employee with id 2 not found")));
		
	}
	
	@Test
	@WithMockUser(authorities = {"ADMIN"})
	public void testAddEmployee() throws Exception {
		
		Employee emp=new Employee(1,"","D","a@abc");
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/employee/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emp));
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest())
		.andExpect(result->assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
		
		emp=new Employee(2,"ABC","DEF","abc@abc.com");
		Mockito.when(employeeService.addEmployee(ArgumentMatchers.any())).thenReturn(2);
		requestBuilder=MockMvcRequestBuilders.post("/employee/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emp));
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.Message", is("Employee with id 2 successfully saved")));
		
		emp=null;
		requestBuilder=MockMvcRequestBuilders.post("/employee/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emp));
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest())
		.andExpect(result->assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException))
		.andExpect(jsonPath("$.Message",is("Employee details is missing")));
		
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testUpdateEmployee() throws Exception {
		
		Employee emp=new Employee(1,"F","G","f@g.com");
		Mockito.when(employeeService.updateEmployee(ArgumentMatchers.any())).thenReturn(1);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.put("/employee/update").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emp));
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.Message", is("Employee with id 1 is updated successfully")));
		
		Mockito.when(employeeService.updateEmployee(ArgumentMatchers.any())).thenThrow(new EmployeeNotFoundException("Employee with id "+emp.getId()+" not found"));
		requestBuilder=MockMvcRequestBuilders.put("/employee/update").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emp));
		mockMvc.perform(requestBuilder)
		.andExpect(status().isNotFound())
		.andExpect(result->assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
		.andExpect(jsonPath("$.Message",is("Employee with id "+emp.getId()+" not found")));
		
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testDeleteEmployee() throws Exception {
		
		Employee emp=new Employee(1,"F","G","f@g.com");
		Mockito.when(employeeService.deleteEmployee(1)).thenReturn(emp);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.delete("/employee/delete/{id}",1).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.Message",is("Deleted Employee: "+emp)));
		
		Mockito.when(employeeService.deleteEmployee(ArgumentMatchers.anyInt())).thenThrow(new EmployeeNotFoundException("Employee with id 1 not found"));
		requestBuilder=MockMvcRequestBuilders.delete("/employee/delete/{id}",1).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder)
		.andExpect(status().isNotFound())
		.andExpect(result->assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
		.andExpect(result->assertEquals("Employee with id 1 not found", result.getResolvedException().getMessage()));
	}

}
