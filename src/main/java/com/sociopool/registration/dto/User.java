package com.sociopool.registration.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long userId;

	@Size(min = 1, max = 35, message = "First Name must be greater than 1 and less than 35 chars!!")
	@Pattern(regexp = "^[A-Za-z0-9!@#$%&()\\-,.'\\/+\\s]*$", message = "Valid chars for username are ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !@#$%&()-,.'/")
	private String username;

	@Size(min = 1, max = 35, message = "First Name must be greater than 1 and less than 35 chars!!")
	@Pattern(regexp = "^[A-Za-z0-9!@#$%&()\\-,.'\\/+\\s]*$", message = "Valid chars for First Name are ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !@#$%&()-,.'/")
	private String password;

	@Transient
	private String confirmPassword;

}
