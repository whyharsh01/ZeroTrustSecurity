package com.harsh.zeroTrust.repository;

import com.harsh.zeroTrust.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}