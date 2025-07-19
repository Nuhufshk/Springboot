package com.group18.ideohub.service.chat;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group18.ideohub.service.UserService;
import com.group18.ideohub.repo.chat.ChatRepository;
import com.group18.ideohub.model.chat.ChatModel;

@Service
public class ChatService {
    private final ChatClient chatClient;

    private UserService UserService;

    private ChatRepository chatRepository;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    // this method sends a user prompt to the AI and returns the AI's response
    public String getAiResponse(String userPrompt) {
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }

    public String processChatMessage(String userPrompt) {
        // get the current user
        String userId = UserService.getCurrentUser();
        saveChatMessage(userId, userPrompt, "user");

        // get the AI's response
        String aiResponse = getAiResponse(userPrompt);
        saveChatMessage(userId, aiResponse, "ai");

        // return the AI's response
        return aiResponse;
    }

    @Transactional
    public void saveChatMessage(String userId, String userPrompt, String sender) {
        // Create a new chat message object
        ChatModel chatMessage = new ChatModel();

        chatMessage.setId(java.util.UUID.randomUUID().toString());
        chatMessage.setUserId(userId);
        chatMessage.setMessage(userPrompt);
        chatMessage.setSender(sender);
        chatMessage.setTime(java.time.LocalDateTime.now());

        // Save the chat message to the repository
        chatRepository.save(chatMessage);
    }

    public List<ChatModel> getAllMessages() {
        return chatRepository.findByUserId(UserService.getCurrentUser());
    }

}
