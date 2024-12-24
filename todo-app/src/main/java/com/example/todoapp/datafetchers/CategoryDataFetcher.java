package com.example.todoapp.datafetchers;

import com.example.todoapp.entity.Category;
import com.example.todoapp.repository.CategoryRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class CategoryDataFetcher {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryDataFetcher(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @DgsQuery
    public List<Category> categories(){
        return categoryRepository.findAll();
    }

    @DgsQuery
    public Optional<Category> category(@InputArgument Long id){
        return categoryRepository.findById(id);
    }


    @DgsMutation
    public Category createCategory(@InputArgument("category") Category category){
        return categoryRepository.save(category);
    }
    @DgsMutation
    public Category updateCategory(@InputArgument Long id, @InputArgument("category") Category category){
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category not found");
        }
        category.setId(id);
        return categoryRepository.save(category);
    }

    @DgsMutation
    public Boolean deleteCategory(@InputArgument Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }
}