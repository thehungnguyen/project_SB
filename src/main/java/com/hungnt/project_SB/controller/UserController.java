package com.hungnt.project_SB.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.request.UserUpdateReq;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateReq req) throws MessagingException {
        return userService.createUser(req);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<UserResponse>> getUsers() throws JsonProcessingException {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping("/{userId}")
    @PostAuthorize("returnObject.username == authentication.name")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateReq req) {
        return userService.updateUser(userId, req);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }
}
