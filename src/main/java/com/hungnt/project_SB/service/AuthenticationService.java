package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.dto.request.VerifindTokenRequest;
import com.hungnt.project_SB.dto.response.AuthenticationResponse;
import com.hungnt.project_SB.dto.response.VerifindTokenResponse;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.signerkey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        boolean authenticate = authenticationRequest.getPassword().equals(user.getPassword());

        if(!authenticate) throw new AppException(ErrorCode.UNAUTHENTICATED);
        else{
            var token = generateToken(authenticationRequest.getUsername());

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(token);
            authenticationResponse.setAuthenticated(true);

            return authenticationResponse;
        }
    }

    private String generateToken(String username){
        // header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // body
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username) // nguoi dang nhap
                .issuer("hung.nt") // nguoi tao token
                .issueTime(new Date()) // thoi gian tao
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) // thoi gian het han
                .build();

        // payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Json web signature
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        // Signature
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException e){
            log.error("Can't create token", e);
            throw new RuntimeException(e);
        }
    }

    public VerifindTokenResponse verifindToken(VerifindTokenRequest verifindTokenRequest) throws JOSEException, ParseException {
        var token = verifindTokenRequest.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        VerifindTokenResponse verifindTokenResponse = new VerifindTokenResponse();
        verifindTokenResponse.setValidToken(verified && expirationTime.after(new Date()));
        return verifindTokenResponse;
    }
}
