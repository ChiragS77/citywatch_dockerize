package com.complaintservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_location", columnList = "district,taluka"),
        @Index(name = "idx_feed", columnList = "district,taluka,wardNo"),
        @Index(name = "idx_status", columnList = "wardNo,status"),
        @Index(name = "idx_type", columnList = "wardNo,type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private String locationText;

    private Boolean isDeleted = false;

    private LocalDateTime assignedAt;

    private String receiverEmail;

    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer wardNo;

    private String username;

    private String district;
    private String taluka;

    @Column( updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private WorkType workType;


    private String updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long workerId;

    private Double amount;
    private String workImageUrl; // 🔥 NEW FIELD



}
