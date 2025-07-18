package com.group18.ideohub.model.chat;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection = "chat")
public class ChatModel {
    @Id
    private String id;

    private String userId; // Reference to User.id
    private String sender; // "user" or "ai"
    private String message;

    private LocalDateTime time;// time

}
