package com.example.EnterpriseSystemsArchitecture.jms;

import com.example.EnterpriseSystemsArchitecture.config.JmsConfig;
import com.example.EnterpriseSystemsArchitecture.dto.EntityChangeEvent;
import com.example.EnterpriseSystemsArchitecture.model.AuditLog;
import com.example.EnterpriseSystemsArchitecture.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener {

    private static final Logger log = LoggerFactory.getLogger(AuditLogListener.class);
    private final AuditLogRepository auditLogRepository;

    public AuditLogListener(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @JmsListener(destination = JmsConfig.ENTITY_EVENTS_TOPIC)
    public void onMessage(EntityChangeEvent event) {
        log.info("[JMS AuditLogListener] Получено событие: {}", event);

        if (event == null || event.getActionType() == null || event.getEntityName() == null) {
            log.warn("[JMS AuditLogListener] Пропущено некорректное сообщение");
            return;
        }

        AuditLog auditLog = new AuditLog(
                event.getActionType(),
                event.getEntityName(),
                event.getEntityId(),
                event.getDetails()
        );

        auditLogRepository.save(auditLog);
        log.info("[JMS AuditLogListener] Запись успешно добавлена в таблицу audit_logs (ID: {})", auditLog.getId());
    }
}