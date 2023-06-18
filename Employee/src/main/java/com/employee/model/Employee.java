package com.employee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(message="Id should not be null")
	@Min(value = 0,message="Id should not be negative")
	private int id;
	
	@NotNull(message = "First Name should not be empty")
	@NotBlank(message = "First Name should not be blank")
	private String firstName;
	
	@NotNull(message="Last Name should not be empty")
	@NotBlank(message="Last Name should not be blank")
	private String lastName;
	
	@Email(message="Enter correct Email Address",regexp="^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}")
	private String email;

	public Employee() {
		
	}

	public Employee(int id,String firstName, String lastName, String email) {
		super();
		this.id=id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "}";
	}
	
	
}
