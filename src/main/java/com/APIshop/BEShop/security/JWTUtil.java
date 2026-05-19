package com.APIshop.BEShop.security;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.APIshop.BEShop.config.UserInfoConfig;
import com.APIshop.BEShop.payloads.dto.user.RoleDTO;
import com.APIshop.BEShop.payloads.dto.user.UserDTO;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String jwt_key;

    public UserInfoConfig getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            var jwt = jwtAuth.getToken();

            return new UserInfoConfig(
                    jwt.getClaimAsString("userId"),
                    jwt.getClaimAsStringList("scope"));
        }
        return null;
    }

    public String generateToken(UserDTO userDTO) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject("User")
                .issuer("Auth")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(60, ChronoUnit.MINUTES).toEpochMilli()))
                .claim("userId", userDTO.getUserId())
                .claim("scope", buildScope(userDTO))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(jwt_key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(UserDTO userDTO) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject("RefreshToken")
                .issuer("Auth")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
                .claim("userId", userDTO.getUserId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(jwt_key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> buildScope(UserDTO userDTO) {
        if (CollectionUtils.isEmpty(userDTO.getRoles())) {
            return Collections.emptyList();
        }
        return userDTO.getRoles()
                .stream()
                .map(RoleDTO::getRoleName)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(jwt_key.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        return verified && expiryDate.after(new Date());
    }

    public UserInfoConfig extractToken(String token) throws ParseException, JOSEException {
        UserInfoConfig userInfoConfig = new UserInfoConfig();
        JWSObject jwsObject = JWSObject.parse(token);
        Map<String, Object> object = jwsObject.getPayload().toJSONObject();
        userInfoConfig.setUserId((String) object.get("userId"));
        Object scope = object.get("scope");
        if (scope instanceof List<?> list) {
            userInfoConfig.setRoles(list.stream()
                    .filter(s -> s instanceof String)
                    .map(s -> (String) s)
                    .collect(Collectors.toList()));
        }
        return userInfoConfig;
    }

}
