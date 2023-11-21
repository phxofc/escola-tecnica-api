package com.escolatecnica.api.user.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.user.auth.data.UserDetailsData;
import com.escolatecnica.api.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationRequest(request);
        setDetails(request, authenticationToken);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        UserDetailsData userData = (UserDetailsData) authResult.getPrincipal();

        if (userData.getUser().getEnabledAccess()) {
            response.setStatus(200);
            response.getWriter().write(buildToken(userData));
        } else {
            response.setStatus(403);
            response.getWriter().write("User does not have permission to access the system.");
        }

        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write(failed.getMessage());
    }

    private UsernamePasswordAuthenticationToken getAuthenticationRequest(HttpServletRequest request)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            String username = String.format("%s%s%s", user.getCpf(), ";", user.getOrganizationId());

            return new UsernamePasswordAuthenticationToken(username, user.getPassword(), new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to authenticate user", e);
        }
    }

    private String buildToken(UserDetailsData userData) {
        return JWT.create()
                .withClaim("cpf", userData.getUsername())
                .withClaim("userId", userData.getUser().getId().toString())
                .withClaim("organizationId", userData.getUser().getOrganizationId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtUtil.getExpiration() * 1000)))
                .withClaim("role", userData.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .sign(Algorithm.HMAC512(jwtUtil.getSecret()));
    }
}
