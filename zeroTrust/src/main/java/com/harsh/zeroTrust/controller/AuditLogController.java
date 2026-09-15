package com.harsh.zeroTrust.controller;

import com.harsh.zeroTrust.entity.AuditLog;
import com.harsh.zeroTrust.repository.AuditLogRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
}