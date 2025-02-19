package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.dto.request.LogoutRequest;
import com.hungnt.project_SB.dto.request.RefreshTokenRequest;
import com.hungnt.project_SB.dto.request.VerifindTokenRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.AuthenticationResponse;
import com.hungnt.project_SB.dto.response.VerifindTokenResponse;
import com.hungnt.project_SB.service.AuthenticationService;
import com.hungnt.project_SB.service.QueueRedisSerivce;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final QueueRedisSerivce queueRedisSerivce;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return queueRedisSerivce.login(authenticationRequest);
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        return authenticationService.logout(logoutRequest);
    }

    @PostMapping("/verifindtoken")
    ApiResponse<VerifindTokenResponse> verification(@RequestBody VerifindTokenRequest verifindTokenRequest) throws ParseException, JOSEException {
        return authenticationService.verifyToken(verifindTokenRequest);
    }

    @PostMapping("/refreshToken")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        return authenticationService.refreshToken(refreshTokenRequest);
    }
}
