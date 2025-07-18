package com.group18.ideohub.repo;

import com.group18.ideohub.model.PostCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostCollectionRepository extends MongoRepository<PostCollection, String> {

}
