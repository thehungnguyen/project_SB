package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.AuthenticationRequest;
import com.hungnt.project_SB.dto.request.LogoutRequest;
import com.hungnt.project_SB.dto.request.RefreshTokenRequest;
import com.hungnt.project_SB.dto.request.VerifindTokenRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.AuthenticationResponse;
import com.hungnt.project_SB.dto.response.VerifindTokenResponse;
import com.hungnt.project_SB.entity.InvalidToken;
import com.hungnt.project_SB.entity.Permission;
import com.hungnt.project_SB.entity.Role;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.exception.AppException;
import com.hungnt.project_SB.exception.ErrorCode;
import com.hungnt.project_SB.repository.InvalidTokenRepository;
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
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvalidTokenRepository invalidTokenRepository;
    @Value("${jwt.signerkey}")
    protected String SIGNER_KEY;
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    // Dang nhap ~ Login
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        boolean authenticate = authenticationRequest.getPassword().equals(user.getPassword());

        if (!authenticate) throw new AppException(ErrorCode.UNAUTHENTICATED);
        else {
            var token = generateToken(user);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(token);
            authenticationResponse.setAuthenticated(true);

            return authenticationResponse;
        }
    }

    // Dang xuat ~ Logout
    public ApiResponse<String> logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            //Lay ra id token va thoi gian het han token
            var signToken = signedJwtToken(logoutRequest.getToken(), false);

            String idJwt = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidToken invalidToken = new InvalidToken();
            invalidToken.setId(idJwt);
            invalidToken.setExpiryTime(expiryTime);

            invalidTokenRepository.save(invalidToken);

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setResult("Account has been logged out");

            return apiResponse;
        } catch (AppException exception) {
            log.info("Token already expired");
            return null;
        }
    }

    // Tao token
    private String generateToken(User user) {
        // header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // body
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                // token id
                .jwtID(UUID.randomUUID().toString())
                // nguoi dang nhap
                .subject(user.getUsername())
                // nguoi tao token
                .issuer("hung.nt")
                // role
                .claim("scope", biuldScope(user))
                // thoi gian tao
                .issueTime(new Date())
                // thoi gian het han
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .build();

        // payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Json web signature
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        // Signature
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can't create token", e);
            throw new RuntimeException(e);
        }
    }

    // Kiem tra token co dung khong
    public ApiResponse<VerifindTokenResponse> verifyToken(VerifindTokenRequest verifindTokenRequest) throws ParseException, JOSEException {
        var token = verifindTokenRequest.getToken();
        boolean isValid = true;

        // Neu co loi AppException thi isValid = false
        try {
            var jwtToken = signedJwtToken(token, false);
        } catch (AppException appException) {
            isValid = false;
        }

        VerifindTokenResponse verifindTokenResponse = new VerifindTokenResponse();
        verifindTokenResponse.setValidToken(isValid);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(verifindTokenResponse);

        return apiResponse;
    }

    // Lam moi token
    public ApiResponse<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        // Kiem tra thoi gian hieu luc cua token
        var signedJWT = signedJwtToken(refreshTokenRequest.getToken(), true);

        var idToken = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Huy token cu ~ logout
        InvalidToken invalidToken = new InvalidToken();
        invalidToken.setId(idToken);
        invalidToken.setExpiryTime(expiryTime);

        invalidTokenRepository.save(invalidToken);

        // Lay thong tin username tu token
        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // Su dung generateToken de tao ra Token moi
        var token = generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticated(true);
        authenticationResponse.setToken(token);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(authenticationResponse);

        return apiResponse;
    }

    // do quy dinh trong oauth2 giua các role canh nhau bang " "
    private String biuldScope(User user) {
        String result = "";
        Set<Role> rolesUser = user.getRoles();

        for (Role r : rolesUser) {
            result += "ROLE_" + r.getName() + " ";
            if (!CollectionUtils.isEmpty(r.getPermissions())) {
                for (Permission p : r.getPermissions()) {
                    result += p.getName() + " ";
                }
            }
        }

        return result.trim();
    }

    // Kiem dinh phan tich token
    private SignedJWT signedJwtToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // Xac thuc bang MACVerifier
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Phan tich token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // lay ra truong ExpiryTime
        // true -> refresh token; false -> verify for authenticate
        Date expirationTime = (isRefresh) ?
                // Thoi gian con hieu luc de cho phep refresh token
                new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                // Thoi gian song cua token
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        // Xac thuc so sanh giua SIGNER_KEY vs token client
        var verified = signedJWT.verify(jwsVerifier);

        // Neu token khong khop va het han thi tra ve exception Unauthenticated
        if (!(verified && expirationTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Neu token da trong danh sach logout thi
        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
}
