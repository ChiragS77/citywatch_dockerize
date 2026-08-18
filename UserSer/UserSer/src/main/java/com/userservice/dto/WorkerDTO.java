package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkerDTO {


    private Long id;

    private String name;

    private String email;

    private String profileImage;

    private String workType;

    private String district;

    private String taluka;

    private Double rating;

    private Boolean available;

    private String upiId;

    private String qrImageUrl;

    private String experience;
}
