package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(AuthenticationRequest authenticationRequest){
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        if(authenticationRequest.getPassword().equals(user.getPassword())){
            return true;
        }
        else return false;
    }
}
