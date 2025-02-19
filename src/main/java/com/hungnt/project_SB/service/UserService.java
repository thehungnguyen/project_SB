package com.hungnt.project_SB.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.request.UserUpdateReq;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.entity.Role;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.RoleRepository;
import com.hungnt.project_SB.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRedisService userRedisService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ApiResponse<UserResponse> createUser(UserCreateReq req) throws MessagingException {
        User user = new User();

        // Tim trong DB da ton tai Username nay chua
        String username = req.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        } else {
            user.setUsername(username);

            user.setPassword(req.getPassword());
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            user.setDob(req.getDob());

            // Set mac dinh role USER cho new User
            HashSet<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOTFOUND));
            roles.add(userRole);
            user.setRoles(roles);
        }

        // Set Email, kiem tra Email da ton tai trong User khac chua
        String email = req.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        } else {
            user.setEmail(email);
        }

        // Save User
        userRepository.save(user);

        // Kafka - Message Queue
        kafkaTemplate.send("sendEmail", user);

        // Gan User cho UserResponse
        UserResponse userResponse = mapUserMTR(user);

        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(userResponse);
        // Neu Create User thanh cong -> xoa bo nho Redis
        if (apiResponse.getCode() == 1000) userRedisService.clear();
        return apiResponse;
    }

    public ApiResponse<List<UserResponse>> getUsers() throws JsonProcessingException {
        List<UserResponse> userResponses = userRedisService.getAllUsers();

        // Neu trong redis khong co du lieu thi lay du lieu trong DB va luu vao Redis
        if (userResponses.isEmpty()) {
            List<User> users = userRepository.findAll();
            userResponses = users.stream().map(this::mapUserMTR).toList();

            userRedisService.saveAllUsers(userResponses);
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userResponses);
        return apiResponse;
    }


    public ApiResponse<UserResponse> getUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        UserResponse userResponse = mapUserMTR(user);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userResponse);
        return apiResponse;
    }

    public ApiResponse<UserResponse> getMyInfo() {
        // Lay username tu token
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        UserResponse userResponse = mapUserMTR(user);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userResponse);
        return apiResponse;
    }

    public ApiResponse<UserResponse> updateUser(String userId, UserUpdateReq req) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());

        var roles = roleRepository.findAllById(req.getRoles());
        user.setRoles(new HashSet<>(roles));

        user = userRepository.save(user);

        UserResponse userResponse = mapUserMTR(user);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userResponse);
        // Neu Update User thanh cong -> xoa bo nho Redis
        if (apiResponse.getCode() == 1000) userRedisService.clear();
        return apiResponse;
    }

    public ApiResponse<String> deleteUser(String userId) {
        userRepository.deleteById(userId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("User has been deleted");
        // Neu Delete User thanh cong -> xoa bo nho Redis
        if (apiResponse.getCode() == 1000) userRedisService.clear();
        return apiResponse;
    }

    private UserResponse mapUserMTR(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setDob(user.getDob());
        userResponse.setRoles(user.getRoles());

        return userResponse;
    }

}