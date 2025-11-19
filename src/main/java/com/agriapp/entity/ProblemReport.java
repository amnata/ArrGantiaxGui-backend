package com.agriapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "problem_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private LocalDateTime timestamp;
    
    @Column(length = 50)
    private String status; 
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "ticket_number")
    private String ticketNumber;
}