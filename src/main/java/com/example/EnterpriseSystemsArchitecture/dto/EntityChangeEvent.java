package com.example.EnterpriseSystemsArchitecture.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EntityChangeEvent implements Serializable {

    private String actionType;
    private String entityName;
    private Long entityId;
    private String details;
    private LocalDateTime timestamp = LocalDateTime.now();

    public EntityChangeEvent() {
    }

    public EntityChangeEvent(String actionType, String entityName, Long entityId, String details) {
        this.actionType = actionType;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
    }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "[" + actionType + "] " + entityName + " (ID: " + entityId + "): " + details;
    }
}
