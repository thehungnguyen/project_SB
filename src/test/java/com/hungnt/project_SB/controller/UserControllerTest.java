package com.hungnt.project_SB.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreateReq userCreateReq;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2002, 12, 22);

        userCreateReq = new UserCreateReq();
        userCreateReq.setUsername("HungNT");
        userCreateReq.setFirstName("Hung");
        userCreateReq.setLastName("Nguyen");
        userCreateReq.setPassword("12345678");
        userCreateReq.setDob(dob);

        userResponse = new UserResponse();
        userResponse.setId("sajdhakdyuas");
        userResponse.setUsername("HungNT");
        userResponse.setFirstName("Hung");
        userResponse.setLastName("Nguyen");
        userResponse.setDob(dob);
    }

    @Test
    void createUser_success() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreateReq);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("HungNT"));
    }

    @Test
    void createUser_invalid_input() throws Exception {
        // Given
        userCreateReq.setUsername("hung");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreateReq);

        // When + Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))

                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 5 characters"));
    }
}
