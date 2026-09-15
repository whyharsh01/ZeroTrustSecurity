package com.harsh.zeroTrust.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    JwtEncoder jwtEncoder;
    JwtService jwtService;

    @BeforeEach
    void setup() {

        jwtEncoder = mock(JwtEncoder.class);

        jwtService = new JwtService();

        // private fields ko set karna padega
        org.springframework.test.util.ReflectionTestUtils.setField(
                jwtService,
                "jwtEncoder",
                jwtEncoder
        );

        org.springframework.test.util.ReflectionTestUtils.setField(
                jwtService,
                "issuer",
                "zero-trust-api"
        );

        org.springframework.test.util.ReflectionTestUtils.setField(
                jwtService,
                "expiry",
                3600L
        );
    }

    @Test
    void generateTokenReturnsJwt() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "harsh1",
                        null,
                        List.of(
                                new SimpleGrantedAuthority("USER_READ"),
                                new SimpleGrantedAuthority("USER_CREATE")
                        )
                );

        Jwt jwt = mock(Jwt.class);

        when(jwt.getTokenValue())
                .thenReturn("test-jwt-token");

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        String token =
                jwtService.generateToken(authentication);

        assertEquals("test-jwt-token", token);

        verify(jwtEncoder)
                .encode(any(JwtEncoderParameters.class));
    }
}