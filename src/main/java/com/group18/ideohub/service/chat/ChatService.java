package com.group18.ideohub.service.chat;

import com.group18.ideohub.model.chat.ChatModel;
import com.group18.ideohub.repo.chat.ChatRepository;
import com.group18.ideohub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient.Builder chatClientBuilder;
    private final UserService userService;
    private final ChatRepository chatRepository;

    public String getAiResponse(String userPrompt) {
        return chatClientBuilder.build().prompt()
                .user(userPrompt)
                .call()
                .content();
    }

    public String processChatMessage(String userPrompt) {
        String userId = userService.getCurrentUser();
        saveChatMessage(userId, userPrompt, "user");

        String aiResponse = getAiResponse(userPrompt);
        saveChatMessage(userId, aiResponse, "ai");

        return aiResponse;
    }

    @Transactional
    public void saveChatMessage(String userId, String userPrompt, String sender) {
        ChatModel chatMessage = new ChatModel();
        chatMessage.setId(java.util.UUID.randomUUID().toString());
        chatMessage.setUserId(userId);
        chatMessage.setMessage(userPrompt);
        chatMessage.setSender(sender);
        chatMessage.setTime(java.time.LocalDateTime.now());
        chatRepository.save(chatMessage);
    }

    public List<ChatModel> getAllMessages() {
        return chatRepository.findByUserId(userService.getCurrentUser());
    }
}
