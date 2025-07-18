package com.group18.ideohub.controller.chat;

import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getAiResponse(String userPrompt) {
        return chatClient.call(userPrompt);
    }
}
