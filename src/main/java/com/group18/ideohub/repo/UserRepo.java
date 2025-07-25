package com.group18.ideohub.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.group18.ideohub.model.Users;

@Repository
public interface UserRepo extends MongoRepository<Users, String> {
    Users findByEmail(String email);

    Users findByUserId(String userId);
}