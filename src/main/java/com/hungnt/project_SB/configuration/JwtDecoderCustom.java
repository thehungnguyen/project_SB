package com.hungnt.project_SB.configuration;

import com.hungnt.project_SB.dto.request.VerifindTokenRequest;
import com.hungnt.project_SB.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class JwtDecoderCustom implements JwtDecoder {
    @Value("${jwt.signerkey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException{
        // kiem tra token con Available khong
        try {
            VerifindTokenRequest verifindTokenRequest = new VerifindTokenRequest();
            verifindTokenRequest.setToken(token);

            var response = authenticationService.verifyToken(verifindTokenRequest);

            if (!response.isValidToken()) throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        // Authentication Provider
        // Neu token con hieu luc thi su dung nimbusJwtDecoder de xac thuc
        if (Objects.isNull(nimbusJwtDecoder)) {
            // tao SecretKey voi 1 thuat toan ma hoa
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
