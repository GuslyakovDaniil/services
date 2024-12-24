package com.example.todoapp.datafetchers;

import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class UserDataFetcher {

    private final UserRepository userRepository;

    @Autowired
    public UserDataFetcher(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DgsQuery
    public List<User> users(){
        return userRepository.findAll();
    }

    @DgsQuery
    public Optional<User> user(@InputArgument Long id){
        return userRepository.findById(id);
    }
    @DgsMutation
    public User createUser(@InputArgument("user") User user){
        return userRepository.save(user);
    }

    @DgsMutation
    public User updateUser(@InputArgument Long id, @InputArgument("user") User user){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }
        user.setId(id);
        return userRepository.save(user);
    }
    @DgsMutation
    public Boolean deleteUser(@InputArgument Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}