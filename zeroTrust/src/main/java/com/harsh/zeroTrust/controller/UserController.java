package com.harsh.zeroTrust.controller;

import com.harsh.zeroTrust.dto.UserRegisterRequestDto;
import com.harsh.zeroTrust.dto.UserRegisterResponseDto;
import com.harsh.zeroTrust.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        System.out.println("hello");
        return "hello, you are : " + authentication.getName() + "  " + " and your role is: " + authentication.getAuthorities();
    }

    @PostMapping("register")
    public ResponseEntity<UserRegisterResponseDto> addUser(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        UserRegisterResponseDto userRegisterResponseDto = authService.register(userRegisterRequestDto);
        return ResponseEntity.ok(userRegisterResponseDto);
    }

    @GetMapping("/token")
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}
