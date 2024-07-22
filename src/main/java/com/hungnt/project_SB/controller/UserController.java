package com.hungnt.project_SB.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.request.UserUpdateReq;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.service.UserRedisService;
import com.hungnt.project_SB.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserRedisService userRedisService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateReq req) throws MessagingException {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(req));
        // Neu Create User thanh cong -> xoa bo nho Redis
        if (apiResponse.getCode() == 1000) userRedisService.clear();
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() throws JsonProcessingException {
        ApiResponse apiResponse = new ApiResponse();

        // Kiem tra du lieu trong Redis
        List<UserResponse> userResponses = userRedisService.getAllUsers();
        // Neu trong redis khong co du lieu thi lay du lieu trong DB va luu vao Redis
        if (userResponses.isEmpty()) {
            userResponses = userService.getUsers();
            userRedisService.saveAllUsers(userResponses);
        }

        apiResponse.setResult(userResponses);
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.getUser(userId));
        return apiResponse;
    }

    // truyen token nhan thong tin ca nhan
    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyInfo() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.getMyInfo());
        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateReq req) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.updateUser(userId, req));
        // Neu Update User thanh cong -> xoa bo nho Redis
        if (apiResponse.getCode() == 1000) userRedisService.clear();
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("User has been deleted");
        // Neu Delete User thanh cong -> xoa bo nho Redis
        if (apiResponse.getCode() == 1000) userRedisService.clear();
        return apiResponse;
    }
}
