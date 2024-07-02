package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.dto.request.LogoutRequest;
import com.hungnt.project_SB.dto.request.RefreshTokenRequest;
import com.hungnt.project_SB.dto.request.VerifindTokenRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.AuthenticationResponse;
import com.hungnt.project_SB.dto.response.VerifindTokenResponse;
import com.hungnt.project_SB.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationService.authenticate(authenticationRequest);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(result);

        return apiResponse;
    }

    @PostMapping("/verifindtoken")
    ApiResponse<VerifindTokenResponse> verification(@RequestBody VerifindTokenRequest verifindTokenRequest) throws ParseException, JOSEException {
        var result = authenticationService.verifyToken(verifindTokenRequest);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(result);

        return apiResponse;
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("Account has been logged out");

        return apiResponse;
    }

    @PostMapping("/refreshToken")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(refreshTokenRequest);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(result);

        return apiResponse;
    }
}
