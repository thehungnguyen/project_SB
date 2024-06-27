package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.request.UserUpdateReq;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSer {
    @Autowired
    private UserRepo userRepo;

    public User createUser(UserCreateReq req){
        User user = new User();

        if(userRepo.existsByUsername(req.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());

        return userRepo.save(user);
    }

    public List<User> getUser(){
        return userRepo.findAll();
    }

    public User getUser(String id){
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(String userId, UserUpdateReq req){
        User user = getUser(userId);

        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());

        return userRepo.save(user);
    }

    public void deleteUser(String userId){
        userRepo.deleteById(userId);
    }
}
