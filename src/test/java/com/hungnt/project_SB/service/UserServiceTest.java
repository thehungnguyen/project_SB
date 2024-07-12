package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreateReq userCreateReq;
    private UserResponse userResponse;
    private User user;
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

        user = new User();
        user.setId("sajdhakdyuas");
        user.setUsername("HungNT");
        user.setFirstName("Hung");
        user.setLastName("Nguyen");
        user.setPassword("12345678");
        user.setDob(dob);

        userResponse = new UserResponse();
        userResponse.setId("sajdhakdyuas");
        userResponse.setUsername("HungNT");
        userResponse.setFirstName("Hung");
        userResponse.setLastName("Nguyen");
        userResponse.setDob(dob);
    }

    @Test
    void createUser_success(){
        //Given
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        //When
        var res = userService.createUser(userCreateReq);

        //Then
        Assertions.assertThat(res.getId()).isEqualTo("sajdhakdyuas");
        Assertions.assertThat(res.getUsername()).isEqualTo("HungNT");
    }
}
