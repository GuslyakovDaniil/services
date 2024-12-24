package com.example.todoapp.assembler;

import com.example.todoapp.controller.UserController;
import com.example.todoapp.entity.User;
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
public class UserAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getUser(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getUsers()).withRel("users"));
    }
    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities){
        List<EntityModel<User>> userModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(userModels, linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
    }
}
