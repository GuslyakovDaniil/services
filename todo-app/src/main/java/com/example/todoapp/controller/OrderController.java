package com.example.todoapp.controller;

import org.example.todoapi.controllers.OrderApi;
import org.example.todoapi.dto.OrderDTOs;
import org.example.todoapi.dto.ProductDTOs;
import org.example.todoapi.dto.UserDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import com.example.todoapp.assembler.OrderAssembler;
import com.example.todoapp.config.MessageProducer;
import com.example.todoapp.dto.NotificationDTO;
import com.example.todoapp.entity.Order;
import com.example.todoapp.entity.Product;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.OrderRepository;
import com.example.todoapp.repository.ProductRepository;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/orders")
public class OrderController implements OrderApi {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderRepository orderRepository;
    private final OrderAssembler orderAssembler;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public OrderController(OrderRepository orderRepository, OrderAssembler orderAssembler,
                           UserRepository userRepository, ProductRepository productRepository,
                           MessageProducer messageProducer, ObjectMapper objectMapper,  NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.orderAssembler = orderAssembler;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }


    @Override
    public OrderDTOs.OrderResponse createOrder(OrderDTOs.OrderRequest request) {
        if (request == null || request.userId() == null || request.productIds() == null || request.productIds().isEmpty() || request.orderDate() == null) {
            throw new InvalidArgumentException("Invalid order data");
        }
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new NotFoundException("User not found"));
        List<Product> products = productRepository.findAllById(request.productIds());

        if (products.size() != request.productIds().size()) {
            throw new NotFoundException("One or more products not found");
        }
        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setOrderDate(request.orderDate());
        Order savedOrder = orderRepository.save(order);

        NotificationDTO notificationDTO = new NotificationDTO("Created order with id:", "order", savedOrder.getId(), "create");
        sendMessage(notificationDTO, "Create order:");
        notificationService.sendNotification(notificationDTO);
        return mapOrderToOrderResponse(savedOrder);
    }


    @Override
    public OrderDTOs.OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found order with id: " + id));
        NotificationDTO notificationDTO = new NotificationDTO("Get order with id:", "order", order.getId(), "get");
        sendMessage(notificationDTO, "Get order:");
        notificationService.sendNotification(notificationDTO);
        return mapOrderToOrderResponse(order);
    }


    @Override
    public OrderDTOs.OrderResponse updateOrder(Long id, OrderDTOs.OrderRequest request) {
        if (request == null || request.userId() == null || request.productIds() == null || request.productIds().isEmpty() || request.orderDate() == null) {
            throw new InvalidArgumentException("Invalid order data");
        }
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not found order with id: " + id));
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new NotFoundException("User not found"));
        List<Product> products = productRepository.findAllById(request.productIds());
        if (products.size() != request.productIds().size()) {
            throw new NotFoundException("One or more products not found");
        }
        order.setUser(user);
        order.setProducts(products);
        order.setOrderDate(request.orderDate());
        Order updatedOrder = orderRepository.save(order);

        NotificationDTO notificationDTO = new NotificationDTO("Updated order with id:", "order", updatedOrder.getId(), "update");
        sendMessage(notificationDTO, "Update order:");
        notificationService.sendNotification(notificationDTO);
        return mapOrderToOrderResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Could not found order with id " + id);
        }
        NotificationDTO notificationDTO = new NotificationDTO("Deleted order with id:", "order", id, "delete");
        sendMessage(notificationDTO, "Delete order:");
        notificationService.sendNotification(notificationDTO);
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDTOs.OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();
        NotificationDTO notificationDTO = new NotificationDTO("Get all orders", "order", null, "get");
        sendMessage(notificationDTO, "Get all orders:");
        notificationService.sendNotification(notificationDTO);
        return orders.stream().map(this::mapOrderToOrderResponse).collect(Collectors.toList());

    }

    private OrderDTOs.OrderResponse mapOrderToOrderResponse(Order order) {
        UserDTOs.UserResponse userResponse = new UserDTOs.UserResponse(
                order.getUser().getId(),
                order.getUser().getFirstName(),
                order.getUser().getLastName(),
                order.getUser().getEmail(),
                order.getUser().getPhone()
        );

        List<ProductDTOs.ProductResponse> productResponses = order.getProducts().stream()
                .map(product -> new ProductDTOs.ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderDTOs.OrderResponse(
                order.getId(),
                userResponse,
                productResponses,
                order.getOrderDate()
        );
    }
    private void sendMessage(NotificationDTO notificationDTO, String errorPrefix) {
        logger.debug("sendMessage called with notificationDTO: {} and errorPrefix: {}", notificationDTO, errorPrefix); // Add this line
        try {
            messageProducer.sendMessage(objectMapper.writeValueAsString(notificationDTO));
        } catch (JsonProcessingException e) {
            logger.error(errorPrefix + " Error during serialization of notification message", e);
            messageProducer.sendMessage(errorPrefix + " Error during serialization of notification message: " + e.getMessage());
        }
    }
}