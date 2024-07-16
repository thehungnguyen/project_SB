package com.hungnt.project_SB.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRedisService {
    private final ObjectMapper redisObjectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    //Key Redis
    private String key = "all_users";

    // Clear cache
    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    // Lay Users tu Redis
    public List<UserResponse> getAllUsers() throws JsonProcessingException {
        String json = (String) redisTemplate.opsForValue().get(key);
        List<User> users = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<User>>() {}) : null;

        List<UserResponse> userResponses = new ArrayList<>();

        if(users != null) {
            for (User user : users) {
                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setUsername(user.getUsername());
                userResponse.setFirstName(user.getFirstName());
                userResponse.setLastName(user.getLastName());
                userResponse.setDob(user.getDob());
                userResponse.setRoles(user.getRoles());

                userResponses.add(userResponse);
            }
        }

        return userResponses;
    }

    // Luu Users vao Redis
    public void saveAllUsers(List<UserResponse> userResponses) throws JsonProcessingException {
        String json = redisObjectMapper.writeValueAsString(userResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}
