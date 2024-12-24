package com.example.todoapp.assembler;

import com.example.todoapp.controller.CategoryController;
import com.example.todoapp.entity.Category;
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
public class CategoryAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {
    @Override
    public EntityModel<Category> toModel(Category entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CategoryController.class).getCategory(entity.getId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).getCategories()).withRel("categories"));
    }

    @Override
    public CollectionModel<EntityModel<Category>> toCollectionModel(Iterable<? extends Category> entities){
        List<EntityModel<Category>> categoryModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(categoryModels, linkTo(methodOn(CategoryController.class).getCategories()).withSelfRel());
    }
}