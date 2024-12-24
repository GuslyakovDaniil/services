package com.example.todoapp.datafetchers;

import com.example.todoapp.entity.Product;
import com.example.todoapp.repository.ProductRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class ProductDataFetcher {
    private final ProductRepository productRepository;

    @Autowired
    public ProductDataFetcher(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @DgsQuery
    public List<Product> products(){
        return productRepository.findAll();
    }
    @DgsQuery
    public Optional<Product> product(@InputArgument Long id){
        return productRepository.findById(id);
    }

    @DgsMutation
    public Product createProduct(@InputArgument("product") Product product){
        return productRepository.save(product);
    }
    @DgsMutation
    public Product updateProduct(@InputArgument Long id, @InputArgument("product") Product product){
        if(!productRepository.existsById(id)){
            throw new RuntimeException("Product not found");
        }
        product.setId(id);
        return productRepository.save(product);
    }

    @DgsMutation
    public Boolean deleteProduct(@InputArgument Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
}