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

import com.group18.ideohub.model.PostCollection;
import com.group18.ideohub.repo.PostCollectionRepository;

@RestController
@RequestMapping("/api/posts")
public class PostCollectionController {

    @Autowired
    private PostCollectionRepository postRepository;

    @GetMapping
    public List<PostCollection> getAll() {
        return postRepository.findAll();
    }

    @PostMapping
    public PostCollection create(@RequestBody PostCollection post) {
        return postRepository.save(post);
    }

    @GetMapping("/{id}")
    public PostCollection getById(@PathVariable String id) {
        return postRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public PostCollection update(@PathVariable String id, @RequestBody PostCollection post) {
        post.setId(id);
        return postRepository.save(post);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        postRepository.deleteById(id);
    }
}