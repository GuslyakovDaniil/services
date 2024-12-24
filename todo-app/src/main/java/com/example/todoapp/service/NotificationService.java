package com.example.todoapp.service;

import com.example.todoapp.dto.NotificationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper;


    @Autowired
    public NotificationService(SimpMessagingTemplate template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    public void sendNotification(NotificationDTO notificationDTO) {
        try {
            String message = objectMapper.writeValueAsString(notificationDTO);
            template.convertAndSend("/topic/notifications", message);
        } catch (Exception e){
            // Handle exception, such as logging or sending an error message
            System.err.println("Error serializing notificationDTO: " + e.getMessage());
            template.convertAndSend("/topic/notifications", "Error serializing notification message");
        }
    }
    public void sendNotification(String message) {
        template.convertAndSend("/topic/notifications", message);
    }
}