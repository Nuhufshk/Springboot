package com.group18.ideohub.controller;

import com.group18.ideohub.model.CollaboratorCollection;
import com.group18.ideohub.repo.CollaboratorCollectionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorCollectionController {
    @Autowired
    private CollaboratorCollectionRepository collaboratorCollectionRepository;

    @GetMapping
    public List<CollaboratorCollection> getAll() {
        return collaboratorCollectionRepository.findAll();
    }

    @PostMapping
    public CollaboratorCollection create(@RequestBody CollaboratorCollection collaboratorCollection) {
        return collaboratorCollectionRepository.save(collaboratorCollection);
    }

    @GetMapping("/{id}")
    public CollaboratorCollection getById(@PathVariable String id) {
        return collaboratorCollectionRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public CollaboratorCollection update(@PathVariable String id,
            @RequestBody CollaboratorCollection collaboratorCollection) {
        collaboratorCollection.setId(id);
        return collaboratorCollectionRepository.save(collaboratorCollection);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        collaboratorCollectionRepository.deleteById(id);
    }

}
