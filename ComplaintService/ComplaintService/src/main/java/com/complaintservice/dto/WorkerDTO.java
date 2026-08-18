package com.complaintservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerDTO {

    private Long id;

    private String name;        // from User
    private String email;       // from User

    private String profileImage;

    private String workType;    // ELECTRICIAN / PLUMBER etc.

    private String district;
    private String taluka;

    private Double rating;      // optional (future)
    private Integer totalJobs;  // optional (future)

    private Boolean available;
    private Double amount;// is worker free or busy

    private String upiId;

    private String qrImageUrl;
    private String proofImageUrl;

    private Boolean paymentDone;
    private String experience;





}