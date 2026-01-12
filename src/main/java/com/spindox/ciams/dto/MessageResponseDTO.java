package com.spindox.ciams.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MessageResponseDTO {

    private String message;
    private int status;

    public MessageResponseDTO(String message,  int status) {
        this.message = message;
        this.status = status;
    }


}
