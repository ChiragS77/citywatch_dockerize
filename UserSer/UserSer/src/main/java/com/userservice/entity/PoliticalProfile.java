package com.userservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PoliticalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String partyName;

    private String partyImageUrl; // Cloudinary URL

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false , unique = true)
    @JsonBackReference
    private User user;

}
