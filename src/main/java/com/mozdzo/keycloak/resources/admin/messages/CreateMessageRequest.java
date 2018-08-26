package com.mozdzo.keycloak.resources.admin.messages;

import javax.validation.constraints.NotBlank;

public class CreateMessageRequest {
    @NotBlank
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
