package com.example.todoapp.config;

import com.example.todoapp.dto.NotificationDTO;
import com.example.todoapp.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final RabbitMQConfig rabbitMQConfig;


    @Autowired
    public MessageConsumer(NotificationService notificationService, ObjectMapper objectMapper, RabbitMQConfig rabbitMQConfig) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueName}")
    public void listen(String message) {
        String queueName = rabbitMQConfig.queueName;
        logger.info("Message read from {} : {}", queueName, message);
        logger.debug("Received message from RabbitMQ: {}", message); // Add this line
        NotificationDTO notificationDTO = null;
        try {
            notificationDTO = objectMapper.readValue(message, NotificationDTO.class);
            logger.info("Deserialized NotificationDTO: {}", notificationDTO);
        } catch (IOException e) {
            logger.error("Error during deserialization of message from RabbitMQ: {}", message, e);
            notificationService.sendNotification("Error during deserialization of message from RabbitMQ: " + e.getMessage());
            return;
        }
        notificationService.sendNotification(notificationDTO);
        logger.debug("Message processed and notification sent: {}", notificationDTO); // Add this line

    }
}