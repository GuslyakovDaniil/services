package com.example.todoapp.assembler;

import com.example.todoapp.controller.OrderController;
import com.example.todoapp.entity.Order;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(OrderController.class).getOrder(entity.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getOrders()).withRel("orders"));
    }
    @Override
    public CollectionModel<EntityModel<Order>> toCollectionModel(Iterable<? extends Order> entities){
        List<EntityModel<Order>> orderModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(orderModels, linkTo(methodOn(OrderController.class).getOrders()).withSelfRel());
    }
}
