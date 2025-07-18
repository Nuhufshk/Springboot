package com.group18.ideohub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group18.ideohub.model.UserCollection;
import com.group18.ideohub.repo.UserCollectionRepository;

@RestController
@RequestMapping("/api/users")

public class UserCollectionController {

    @Autowired
    private UserCollectionRepository userRepository;

    @GetMapping
    public List<UserCollection> getAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public UserCollection create(@RequestBody UserCollection user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public UserCollection getById(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public UserCollection update(@PathVariable String id, @RequestBody UserCollection user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userRepository.deleteById(id);
    }
}