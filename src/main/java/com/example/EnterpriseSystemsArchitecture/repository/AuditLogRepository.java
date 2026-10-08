package com.example.EnterpriseSystemsArchitecture.repository;

import com.example.EnterpriseSystemsArchitecture.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}