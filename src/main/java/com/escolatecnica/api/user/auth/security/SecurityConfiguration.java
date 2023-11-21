package com.escolatecnica.api.user.auth.security;

import com.escolatecnica.api.root.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        return http
                .cors(Customizer.withDefaults())
                .csrf().disable()
                .authorizeHttpRequests(authorizationRegistry -> {
                    authorizationRegistry.requestMatchers(HttpMethod.POST, "/api/login").permitAll();
                    authorizationRegistry.requestMatchers(HttpMethod.POST, "/enrollment/").permitAll();
                    authorizationRegistry.requestMatchers(HttpMethod.GET, "/organization").permitAll();
                    authorizationRegistry.requestMatchers(HttpMethod.GET, "/organization/login").permitAll();
                    authorizationRegistry.requestMatchers(HttpMethod.GET, "/course/by-organization/*").permitAll();
                    authorizationRegistry.requestMatchers(HttpMethod.GET, "/clazz/login/*/*").permitAll();
                    authorizationRegistry.anyRequest().authenticated();
                })
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTAuthenticationFilter(
                        authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil))
                .addFilter(new JWTAuthorizationFilter(
                        authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
