package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.UserCreateReq;
import com.hungnt.project_SB.dto.request.UserUpdateReq;
import com.hungnt.project_SB.dto.response.UserResponse;
import com.hungnt.project_SB.entity.Role;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.RoleRepository;
import com.hungnt.project_SB.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public User createUser(UserCreateReq req){
        // Tim trong DB da ton tai Username nay chua
        if(userRepository.existsByUsername(req.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = new User();

        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());

        // Set mac dinh role USER cho new User
        HashSet<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOTFOUND));
        roles.add(userRole);

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    //@PreAuthorize("hasAuthority('CREATE_POST')")
    public List<User> getUser(){
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User getUser(String id){
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
    }

    public UserResponse getMyInfo(){
        // Lay username tu token
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setDob(user.getDob());
        userResponse.setRoles(user.getRoles());

        return userResponse;
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public User updateUser(String userId, UserUpdateReq req){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());

        var roles = roleRepository.findAllById(req.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

}
