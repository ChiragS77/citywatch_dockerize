package com.complaintservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "likes",
        indexes = {
                @Index(name = "idx_post_like", columnList = "postId")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"postId", "username"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;
    private String username;
}
