package com.group18.ideohub.response.chat;

public class ChatRequest {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatRequest(String message) {
        this.message = message;
    }

    public ChatRequest() {
    }
}
