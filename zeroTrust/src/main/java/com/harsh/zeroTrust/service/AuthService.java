package com.harsh.zeroTrust.service;

import com.harsh.zeroTrust.dto.UserRegisterRequestDto;
import com.harsh.zeroTrust.dto.UserRegisterResponseDto;
import com.harsh.zeroTrust.dto.UserResponseDto;
import com.harsh.zeroTrust.entity.AuditLog;
import com.harsh.zeroTrust.entity.Role;
import com.harsh.zeroTrust.entity.User;
import com.harsh.zeroTrust.repository.AuditLogRepository;
import com.harsh.zeroTrust.repository.RoleRepository;
import com.harsh.zeroTrust.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AuthService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    AuditLogRepository auditLogRepository;
    PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuditLogRepository auditLogRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserRegisterResponseDto register(
            UserRegisterRequestDto userRegisterRequestDto) {

        User user = new User();

        user.setUsername(userRegisterRequestDto.getUsername());
        user.setPassword(
                passwordEncoder.encode(userRegisterRequestDto.getPassword())
        );
        user.setEnabled(true);

        Role role = roleRepository.findByName("Role_Admin").get();
        user.getRoles().add(role);

        userRepository.save(user);

        UserRegisterResponseDto response = new UserRegisterResponseDto();
        response.setUsername(user.getUsername());
        response.setMessage(user.getUsername() + " saved successfully");

        return response;
    }

    public UserResponseDto getUserById(Long id, Authentication authentication) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if (!user.getUsername().equals(authentication.getName())) {

            AuditLog auditLog = new AuditLog();
            auditLog.setUsername(authentication.getName());
            auditLog.setAction("GET_USER");
            auditLog.setResource("USER:" + id);
            auditLog.setResult("DENIED");
            auditLog.setTimestamp(LocalDateTime.now());

            auditLogRepository.save(auditLog);

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied"
            );
        }

        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(authentication.getName());
        auditLog.setAction("GET_USER");
        auditLog.setResource("USER:" + id);
        auditLog.setResult("ALLOWED");
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);

        UserResponseDto response = new UserResponseDto();
        response.setId((long) user.getId());
        response.setUsername(user.getUsername());
        response.setEnabled(user.isEnabled());

        return response;
    }
}