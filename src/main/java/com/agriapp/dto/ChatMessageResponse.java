package com.agriapp.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageResponse {
    private String message;
    private LocalDateTime timestamp;
    private String type;
}