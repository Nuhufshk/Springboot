package com.group18.ideohub.repo;

import com.group18.ideohub.model.UserCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollectionRepository extends MongoRepository<UserCollection, String> {
}