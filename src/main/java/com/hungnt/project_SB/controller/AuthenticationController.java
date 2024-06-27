package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.AuthenticationResponse;
import com.hungnt.project_SB.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        boolean result = authenticationService.authenticate(authenticationRequest);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticated(result);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(authenticationResponse);

        return apiResponse;
    }

}
