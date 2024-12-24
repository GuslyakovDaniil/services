package com.example.todoapp.assembler;

import com.example.todoapp.controller.ProductController;
import com.example.todoapp.entity.Product;
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
public class ProductAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>>{
    @Override
    public EntityModel<Product> toModel(Product entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ProductController.class).getProduct(entity.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getProducts()).withRel("products"));
    }
    @Override
    public CollectionModel<EntityModel<Product>> toCollectionModel(Iterable<? extends Product> entities){
        List<EntityModel<Product>> productModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productModels, linkTo(methodOn(ProductController.class).getProducts()).withSelfRel());
    }
}
