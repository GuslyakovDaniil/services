package com.example.todoapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = rabbitMQConfig.queueName;
    }


    public void sendMessage(String message) {
        logger.debug("Attempting to send message to queue '" + queueName + "': " + message);
        try {
            rabbitTemplate.convertAndSend(queueName, message);
            logger.info("Message successfully sent to queue '" + queueName + "': " + message);
        } catch (AmqpException e) {
            logger.error("Error sending message to queue '" + queueName + "': " + message, e);
        }
    }
}