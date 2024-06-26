package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.request.UserUpdateReq;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.service.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserSer userSer;

    @PostMapping
    User createUser(@RequestBody UserCreateReq req){
        return userSer.createUser(req);
    }

    @GetMapping
    List<User> getUser(){
        return userSer.getUser();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable String userId){
        return userSer.getUser(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable String userId, @RequestBody UserUpdateReq req){
        return userSer.updateUser(userId, req);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userSer.deleteUser(userId);
        return "User has been deleted";
    }
}
