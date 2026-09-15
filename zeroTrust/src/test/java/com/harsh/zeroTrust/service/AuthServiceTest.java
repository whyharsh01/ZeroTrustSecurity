package com.harsh.zeroTrust.service;

import com.harsh.zeroTrust.dto.UserResponseDto;
import com.harsh.zeroTrust.entity.AuditLog;
import com.harsh.zeroTrust.entity.User;
import com.harsh.zeroTrust.repository.AuditLogRepository;
import com.harsh.zeroTrust.repository.RoleRepository;
import com.harsh.zeroTrust.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    UserRepository userRepository;
    RoleRepository roleRepository;
    AuditLogRepository auditLogRepository;
    Authentication authentication;

    AuthService authService;

    @BeforeEach
    void setup() {

        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        auditLogRepository = mock(AuditLogRepository.class);
        authentication = mock(Authentication.class);

        authService = new AuthService(
                userRepository,
                roleRepository,
                auditLogRepository
        );
    }

    @Test
    void ownerCanAccessOwnUser() {

        User user = new User();
        user.setId(7);
        user.setUsername("harsh1");
        user.setEnabled(true);

        when(userRepository.findById(7L))
                .thenReturn(Optional.of(user));

        when(authentication.getName())
                .thenReturn("harsh1");

        UserResponseDto response =
                authService.getUserById(7L, authentication);

        assertEquals(7L, response.getId());
        assertEquals("harsh1", response.getUsername());
        assertTrue(response.isEnabled());

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void nonOwnerCannotAccessUser() {

        User user = new User();
        user.setId(8);
        user.setUsername("rahul");
        user.setEnabled(true);

        when(userRepository.findById(8L))
                .thenReturn(Optional.of(user));

        when(authentication.getName())
                .thenReturn("harsh1");

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> authService.getUserById(8L, authentication)
                );

        assertEquals(403, exception.getStatusCode().value());

        verify(auditLogRepository).save(
                argThat(auditLog ->
                        auditLog.getUsername().equals("harsh1")
                                && auditLog.getAction().equals("GET_USER")
                                && auditLog.getResource().equals("USER:8")
                                && auditLog.getResult().equals("DENIED")
                )
        );
    }

    @Test
    void userNotFoundReturns404() {

        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> authService.getUserById(
                                100L,
                                authentication
                        )
                );

        assertEquals(404, exception.getStatusCode().value());

        verifyNoInteractions(auditLogRepository);
    }

    @Test
    void ownerAccessCreatesAllowedAuditLog() {

        User user = new User();
        user.setId(7);
        user.setUsername("harsh1");

        when(userRepository.findById(7L))
                .thenReturn(Optional.of(user));

        when(authentication.getName())
                .thenReturn("harsh1");

        authService.getUserById(7L, authentication);

        verify(auditLogRepository).save(
                argThat(auditLog ->
                        auditLog.getUsername().equals("harsh1")
                                && auditLog.getAction().equals("GET_USER")
                                && auditLog.getResource().equals("USER:7")
                                && auditLog.getResult().equals("ALLOWED")
                                && auditLog.getTimestamp() != null
                )
        );
    }
}