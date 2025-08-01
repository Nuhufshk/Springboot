package com.group18.ideohub.controller.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group18.ideohub.model.chat.ChatModel;
import com.group18.ideohub.response.chat.ChatRequest;
import com.group18.ideohub.response.chat.ChatResponse;
import com.group18.ideohub.service.chat.ChatService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chat")
public class AiChat {

    @Autowired
    private ChatService chatService;

    // get method to get all chat messages
    @GetMapping("/messages")
    public ResponseEntity<ChatResponse<?>> getAllMessages() {
        try {
            return ResponseEntity
                    .ok(new ChatResponse<>(true, "Successfully retrieved messages", chatService.getAllMessages()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ChatResponse<>(false, "Failed to retrieve messages:" + e.getMessage(), null));
        }
    }

    // post method to send a chat message
    @PostMapping("/messages")
    public ResponseEntity<ChatResponse<?>> sendMessage(@RequestBody ChatRequest chatRequest) {
        try {
            ChatModel aiResponse = chatService.processChatMessage(chatRequest.getMessage());
            return ResponseEntity.ok(new ChatResponse<>(true, "Successfully sent message", aiResponse));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ChatResponse<>(false, "Failed to send message:" + e.getMessage(), null));
        }
    }

}
