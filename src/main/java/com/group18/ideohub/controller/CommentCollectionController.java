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

import com.group18.ideohub.model.CommentCollection;
import com.group18.ideohub.repo.CommentCollectionRepository;

@RestController
@RequestMapping("/api/comments")
public class CommentCollectionController {

    @Autowired
    private CommentCollectionRepository commentRepository;

    @GetMapping
    public List<CommentCollection> getAll() {
        return commentRepository.findAll();
    }

    @PostMapping
    public CommentCollection create(@RequestBody CommentCollection comment) {
        return commentRepository.save(comment);
    }

    @GetMapping("/{id}")
    public CommentCollection getById(@PathVariable String id) {
        return commentRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public CommentCollection update(@PathVariable String id, @RequestBody CommentCollection comment) {
        comment.setId(id);
        return commentRepository.save(comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        commentRepository.deleteById(id);
    }
}