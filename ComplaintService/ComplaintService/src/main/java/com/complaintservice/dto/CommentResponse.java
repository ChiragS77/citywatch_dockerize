package com.complaintservice.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
    public class CommentResponse {
        private Long id;
        private String text;
        private String username;

        @CreationTimestamp
        private LocalDateTime createdAt;
    }

