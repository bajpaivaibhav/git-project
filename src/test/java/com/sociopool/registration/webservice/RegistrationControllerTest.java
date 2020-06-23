package com.sociopool.registration.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sociopool.registration.dto.User;
import com.sociopool.registration.services.SecurityService;
import com.sociopool.registration.services.UserService;

@TestInstance(Lifecycle.PER_CLASS)
class RegistrationControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@Mock
	private SecurityService securityService;

	@InjectMocks
	private RegistrationController registrationController;

	@BeforeAll
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
	}

	@Test
	void registerTest() throws Exception {
		User user = new User();
		doThrow(IllegalArgumentException.class).when(userService).save(user);
		doThrow(IllegalArgumentException.class).when(securityService).autoLogin(ArgumentMatchers.anyString(),
				ArgumentMatchers.anyString());
		MvcResult result = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(user)))
				.andReturn();	
		assertEquals(200, result.getResponse().getStatus());
		
		  verify(userService, times(1)).save(Mockito.any());
		  verifyNoMoreInteractions(userService);
		 
		verify(securityService, times(1)).autoLogin(null, null);
		verifyNoMoreInteractions(securityService);

	}
	
	@Test
	void loginTest() throws Exception {
		doThrow(IllegalArgumentException.class).when(securityService).autoLogin(ArgumentMatchers.anyString(),
				ArgumentMatchers.anyString());
		MvcResult result = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andReturn();	
		assertEquals(200, result.getResponse().getStatus());
		
		verify(securityService, times(1)).autoLogin(null, null);
		verifyNoMoreInteractions(securityService);

	}

}
