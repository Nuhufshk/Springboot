package com.group18.ideohub.repo.chat;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.group18.ideohub.model.chat.ChatModel;

//chat repository interface for MongoDB between Chat AI and User
@Repository
public interface ChatRepository extends MongoRepository<ChatModel, String> {
    List<ChatModel> findByUserId(String userId);
}
