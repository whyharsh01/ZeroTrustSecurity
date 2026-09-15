package com.harsh.zeroTrust.controller;

import com.harsh.zeroTrust.dto.UserRegisterRequestDto;
import com.harsh.zeroTrust.dto.UserRegisterResponseDto;
import com.harsh.zeroTrust.dto.UserResponseDto;
import com.harsh.zeroTrust.service.AuthService;
import com.harsh.zeroTrust.service.RateLimitService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final AuthService authService;
    private final RateLimitService rateLimitService;

    public UserController(
            AuthService authService,
            RateLimitService rateLimitService) {

        this.authService = authService;
        this.rateLimitService = rateLimitService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/hello")
    public String hello(Authentication authentication) {

        if (!rateLimitService.allowRequest(authentication.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many requests"
            );
        }

        return "hello, you are : " + authentication.getName()
                + " and your role is: " + authentication.getAuthorities();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(
            @PathVariable Long id,
            Authentication authentication) {

        if (!rateLimitService.allowRequest(authentication.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many requests"
            );
        }

        return ResponseEntity.ok(
                authService.getUserById(id, authentication)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> addUser(
            @Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {

        UserRegisterResponseDto response =
                authService.register(userRegisterRequestDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/token")
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}