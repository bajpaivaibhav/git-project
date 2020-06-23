package com.sociopool.registration.webservice;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sociopool.registration.dto.User;
import com.sociopool.registration.services.SecurityService;
import com.sociopool.registration.services.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "RegLogging")
@RestController
public class RegistrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@ApiOperation(value = "Registartion", nickname = "Sign up")
	@PostMapping(value = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String registration(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@ApiParam(name = "requestBody", value = "Document Signing Request", required = true) @RequestBody(required = true) User user) {
		String response;

		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {

			log.info("Executing call for {}", user);
			// Validating the input parameters

			Validator validator = factory.getValidator();
			Set<ConstraintViolation<User>> violations = validator.validate(user);
			if (violations.isEmpty()) {
				if (!user.getPassword().equals(user.getConfirmPassword())) {
					throw new RuntimeException();
				}
				userService.save(user);

				securityService.autoLogin(user.getUsername(), user.getConfirmPassword());

				response = "User registered and logged in succesfully.";
			} else {
				return violations.stream().findFirst().get().toString();
			}

		} catch (RuntimeException e) {
			log.error("Confirm password and actual password does not match : ", e.getMessage());
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return "Confirm password and actual password does not match.";
		} catch (Exception e) {
			log.error("User already registered.", e.getMessage());
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response = "User already registered.";
		}
		return response;
	}

	@ApiOperation(value = "Login", notes = "enter credentials")
	@PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String login(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@ApiParam(name = "requestBody", value = "Document Signing Request", required = true) @RequestBody(required = true) User user) {
		String response;
		try {
			securityService.autoLogin(user.getUsername(), user.getPassword());
			response = "User logged in successfully.";
		} catch (Exception e) {
			log.error("User not found.", e.getMessage());
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response = "User not found.";
		}
		return response;
	}
}
