package com.spindox.authservice.dto;

import lombok.Data;

@Data
public class MessageResponseDTO {

    private String message;
    private int status;

    public MessageResponseDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
