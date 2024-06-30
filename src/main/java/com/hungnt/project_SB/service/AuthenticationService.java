package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.dto.request.VerifindTokenRequest;
import com.hungnt.project_SB.dto.response.AuthenticationResponse;
import com.hungnt.project_SB.dto.response.VerifindTokenResponse;
import com.hungnt.project_SB.entity.User;
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
import java.util.Set;

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
            var token = generateToken(user);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(token);
            authenticationResponse.setAuthenticated(true);

            return authenticationResponse;
        }
    }

    private String generateToken(User user){
        // header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // body
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                // nguoi dang nhap
                .subject(user.getUsername())
                // nguoi tao token
                .issuer("hung.nt")
                // role
                .claim("scope", biuldScope(user))
                // thoi gian tao
                .issueTime(new Date())
                // thoi gian het han
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
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

    // do quy dinh trong oauth2 giua các role canh nhau bang " "
    private String biuldScope(User user){
        Set<String> rolesUser = user.getRoles();
        String result = "";
        if(!rolesUser.isEmpty()){
            for(String s : rolesUser){
                result += s + " ";
            }
        }
        return result.trim();
    }
}
