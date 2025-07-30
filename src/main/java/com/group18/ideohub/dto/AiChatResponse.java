package com.group18.ideohub.dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class AiChatResponse {
    private String id;
    private String text;
    private String sender;
    private LocalDateTime time;

    public AiChatResponse(String id, String text, String sender, LocalDateTime time) {
        this.id = id;
        this.text = text;
        this.sender = sender;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
