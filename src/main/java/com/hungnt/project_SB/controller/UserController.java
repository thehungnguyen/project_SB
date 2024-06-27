package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.request.UserUpdateReq;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreateReq req){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(req));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUser(){
        ApiResponse apiResponse = new ApiResponse();
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> users = userService.getUser();

        for(User user : users){
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setPassword(user.getPassword());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setDob(user.getDob());

            userResponses.add(userResponse);
        }

        apiResponse.setResult(userResponses);
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<User> getUser(@PathVariable String userId){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.getUser(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<User> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateReq req){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.updateUser(userId, req));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("User has been deleted");
        return apiResponse;
    }
}
