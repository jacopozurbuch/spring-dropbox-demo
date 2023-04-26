package com.example.demo.api.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.api.Service.implementations.UserServiceImpl;
import com.example.demo.api.models.LoginResponse;

@WebMvcTest(UserRessource.class)
public class UserRessourceTest {


	@Autowired
	private MockMvc mockMvc;

	@MockBean
    private UserServiceImpl service;

	@Test
	public void setUserTest() throws Exception {

		JSONObject testUser = new JSONObject()
		.put("name", "manuel black")
		.put("user", "manuel")
		.put("birthday", "1950-01-03")
		.put("email", "manuel.black@gmail.com")
		.put("password", "test1234");

		this.mockMvc.perform(post("/users")
		                     .contentType(MediaType.APPLICATION_JSON)
							 .content(testUser.toString()))
					.andDo(print())		 
		            .andExpect(status().isCreated());
	}


    @Test
    public void loginTest() throws Exception {
        String auth = "Basic YmVydDp0ZXN0MzQ1Ng==";

        LoginResponse logIn = new LoginResponse("password is valid", true);
        when(service.checkAuthCredentials(auth)).thenReturn(logIn);

        this.mockMvc.perform(get("/login")
                             .header("Authorization", auth))
                    .andDo(print())
                    .andExpect(status().isAccepted());         
    }

    
}
