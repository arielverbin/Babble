package com.example.babble.serverObjects;

import com.example.babble.utilities.DateGenerator;
import com.example.babble.entities.Message;

public class ServerMessage {
    private String id;
    private String content;
    private String created;

    private User sender;

    public ServerMessage(String id, String content, String created, User sender) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.sender = sender;
    }

    public Message convertToMessage(String username, String chatId) {
        return new Message(
                this.getContent(),
                chatId,
                DateGenerator.toHour(created),
                DateGenerator.toTimeDay(created),
                this.getSender().getUsername().equals(username));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

}