package com.group18.ideohub.repo;

import com.group18.ideohub.model.CollaboratorCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CollaboratorCollectionRepository extends MongoRepository<CollaboratorCollection, String> {
}