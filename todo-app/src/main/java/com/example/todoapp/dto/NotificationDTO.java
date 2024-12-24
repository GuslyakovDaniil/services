package com.example.todoapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationDTO {
    private String message;
    private String entityType;
    private Long entityId;
    private String operationType;

    public NotificationDTO(String message, String entityType, Long entityId, String operationType) {
        this.message = message;
        this.entityType = entityType;
        this.entityId = entityId;
        this.operationType = operationType;
    }
    @Override
    public String toString(){
        return  "{" +
                "\"message\":" + "\"" + message + "\"" +
                ",\"entityType\":" + "\"" + entityType + "\"" +
                ",\"entityId\":" + entityId +
                ",\"operationType\":" + "\"" + operationType + "\"" +
                "}";
    }
}