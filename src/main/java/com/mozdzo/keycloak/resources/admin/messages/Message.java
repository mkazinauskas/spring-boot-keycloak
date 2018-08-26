package com.mozdzo.keycloak.resources.admin.messages;

public class Message {

    private long id;

    private String message;

    Message() {
    }

    public Message(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
