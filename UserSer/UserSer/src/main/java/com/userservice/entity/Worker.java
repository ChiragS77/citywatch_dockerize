package com.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 LINK WITH USER
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String upiId;
    private String qrImageUrl;

    @Enumerated(EnumType.STRING)
    private WorkType workType; // ELECTRICIAN, PLUMBER

    private String experience;
    private Double rating = 0.0;
    private Boolean available = true;

    private String profileImage;
}