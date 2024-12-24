package com.example.todoapp.datafetchers;

import com.example.todoapp.entity.Order;
import com.example.todoapp.entity.Product;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.OrderRepository;
import com.example.todoapp.repository.ProductRepository;
import com.example.todoapp.repository.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class OrderDataFetcher {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderDataFetcher(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @DgsQuery
    public List<Order> orders(){
        return orderRepository.findAll();
    }
    @DgsQuery
    public Optional<Order> order(@InputArgument Long id){
        return orderRepository.findById(id);
    }
    @DgsMutation
    public Order createOrder(@InputArgument("order") Map<String, Object> orderInput) {
        Long userId = Long.valueOf(orderInput.get("userId").toString());
        List<Long> productIds =  ((List<?>) orderInput.get("productIds")).stream()
                .filter(Objects::nonNull)
                .map(item -> {
                    if (item instanceof Number) {
                        return ((Number) item).longValue();
                    } else if(item instanceof String) {
                        try {
                            return Long.parseLong((String) item);
                        }
                        catch (NumberFormatException e){
                            throw new RuntimeException("Invalid product ID format: " + item, e);
                        }

                    }else {
                        throw new RuntimeException("Invalid product ID type: " + item.getClass().getName());
                    }
                }).collect(Collectors.toList());
        String orderDateString = orderInput.get("orderDate").toString();

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Product> products = productRepository.findAllById(productIds);
        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setOrderDate(LocalDate.parse(orderDateString));
        return orderRepository.save(order);
    }
    @DgsMutation
    public Order updateOrder(@InputArgument Long id, @InputArgument("order")  Map<String, Object> orderInput){
        if(!orderRepository.existsById(id)){
            throw new RuntimeException("Order not found");
        }
        Long userId = Long.valueOf(orderInput.get("userId").toString());
        List<Long> productIds =  ((List<?>) orderInput.get("productIds")).stream()
                .filter(Objects::nonNull)
                .map(item -> {
                    if (item instanceof Number) {
                        return ((Number) item).longValue();
                    } else if(item instanceof String) {
                        try {
                            return Long.parseLong((String) item);
                        }
                        catch (NumberFormatException e){
                            throw new RuntimeException("Invalid product ID format: " + item, e);
                        }

                    }else {
                        throw new RuntimeException("Invalid product ID type: " + item.getClass().getName());
                    }
                }).collect(Collectors.toList());
        String orderDateString = orderInput.get("orderDate").toString();
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Product> products = productRepository.findAllById(productIds);
        order.setUser(user);
        order.setProducts(products);
        order.setOrderDate(LocalDate.parse(orderDateString));
        return orderRepository.save(order);
    }
    @DgsMutation
    public Boolean deleteOrder(@InputArgument Long id){
        if(!orderRepository.existsById(id)){
            return false;
        }
        orderRepository.deleteById(id);
        return true;
    }
}