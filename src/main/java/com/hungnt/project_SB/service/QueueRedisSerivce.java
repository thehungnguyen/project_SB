package com.hungnt.project_SB.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class QueueRedisSerivce {
    private static final String queueName = "loginQueue";
    private final RedisTemplate<String, Object> redisTemplate;

    public void enQueue(String userName){
        redisTemplate.opsForList().leftPush(queueName, userName);
    }

    public String deQueue(){
        return (String) redisTemplate.opsForList().rightPop(queueName, 5, TimeUnit.SECONDS);
    }

    public boolean isEmptyQueue(){ return redisTemplate.opsForList().size(queueName) == 0; }
}
