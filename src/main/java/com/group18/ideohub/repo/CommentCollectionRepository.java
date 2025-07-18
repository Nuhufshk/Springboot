package com.group18.ideohub.repo;

import com.group18.ideohub.model.CommentCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentCollectionRepository extends MongoRepository<CommentCollection, String> {
}
