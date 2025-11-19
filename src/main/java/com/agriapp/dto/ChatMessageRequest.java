package com.agriapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageRequest {
    private String message;
    private LocalDateTime timestamp;
}

