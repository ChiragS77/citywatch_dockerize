package com.userservice.dto;

import com.userservice.entity.Worker;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerResponse {

    private Long id;

    private String name;

    private String email;

    private String profileImage;

    private String workType;

    private String district;

    private String taluka;

    private String experience;

    private Double rating;

    private Boolean available;

    private String upiId;

    private String qrImageUrl;

    public WorkerResponse(Worker worker) {

        this.id = worker.getId();

        if(worker.getUser() != null) {

            this.name = worker.getUser().getName();
            this.email = worker.getUser().getEmail();

            this.district = worker.getUser().getDistrict();
            this.taluka = worker.getUser().getTaluka();
        }

        this.profileImage = worker.getProfileImage();

        this.workType =
                worker.getWorkType() != null
                        ? worker.getWorkType().name()
                        : null;

        this.experience = worker.getExperience();

        this.rating = worker.getRating();

        this.available = worker.getAvailable();

        this.upiId = worker.getUpiId();

        this.qrImageUrl = worker.getQrImageUrl();
    }
}