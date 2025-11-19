package com.agriapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProblemReportRequest {
    private String description;
    private LocalDateTime timestamp;
}