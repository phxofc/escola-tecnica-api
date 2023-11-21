package com.escolatecnica.api.root.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.escolatecnica.api.user.auth.data.UserDetailsData;
import com.escolatecnica.api.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTUtil {
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.expiration}")
    Long expiration;

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public UUID getOrganizationIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return UUID.fromString(getOrganizationIdFromTokenJWT(token));
    }

    public UUID getUserIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return UUID.fromString(getUserIdFromTokenJWT(token));
    }

    public String getRoleFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);

        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
                .getClaim("role").toString().replace("\"", "")
                .replace(" ", "");
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String authorization = getAuthorizationFromHeader(request);
        return getTokenFromAuthorizationHeader(authorization);
    }

    public String getUserIdFromTokenJWT(String token) {
        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
                .getClaim("userId").toString().replace("\"", "")
                .replace(" ", "");
    }

    public String getOrganizationIdFromTokenJWT(String token) {
        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
                .getClaim("organizationId").toString().replace("\"", "")
                .replace(" ", "");
    }

    public String getCpfFromTokenJWT(String token) {
        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
                .getClaim("cpf").toString().replace("\"", "")
                .replace(" ", "");
    }

    private String getTokenFromAuthorizationHeader(String header) {
        return header.replace(TOKEN_PREFIX, "");
    }

    public String getAuthorizationFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean isTokenValid(String token) {
        String cpf = getCpfFromTokenJWT(token);
        return Objects.nonNull(cpf);
    }

}
