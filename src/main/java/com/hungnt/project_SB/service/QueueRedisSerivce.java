package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueRedisSerivce {
    private static final String queueName = "loginQueue";
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationService authenticationService;

    public ApiResponse<AuthenticationResponse> login(AuthenticationRequest authenticationRequest){
        ApiResponse apiResponse = new ApiResponse();

        // Join vao hang doi Login
        enQueue(authenticationRequest.getUsername());
        log.info("Login request received. You are in the queue....");

        // Kiem tra den khi nao userName == authenReq.getUserName()
        while (!isEmptyQueue()) {
            String userName = deQueue();
            if (userName.equals(authenticationRequest.getUsername())) {
                log.info("Processed login for: " + userName);
                var result = authenticationService.authenticate(authenticationRequest);
                apiResponse.setResult(result);
                break;
            }
        }
        return apiResponse;
    }

    public void enQueue(String userName) {
        redisTemplate.opsForList().leftPush(queueName, userName);
    }

    public String deQueue() {
        return (String) redisTemplate.opsForList().rightPop(queueName, 5, TimeUnit.SECONDS);
    }

    public boolean isEmptyQueue() {
        return redisTemplate.opsForList().size(queueName) == 0;
    }
}
