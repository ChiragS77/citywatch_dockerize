package com.complaintservice.dto;

import com.complaintservice.entity.Notification;
import com.complaintservice.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerNotificationDto {

    private Long id;
    private String message;
    private Long complaintId;
    private String status;

    // 🔥 Only required fields
    private String caption;
    private String imageUrl;
    private String district;
    private String taluka;
    private Integer wardNo;
    private String workImageUrl;   // worker proof
    private Double amount;



    public WorkerNotificationDto(Notification n, Post p) {

        this.id = n.getId();
        this.message = n.getMessage();
        this.complaintId = n.getComplaintId();
        this.status = n.getStatus().name();

        this.amount = n.getAmount();
        this.workImageUrl = n.getProofImageUrl();

        if (p != null) {

            this.caption = p.getCaption();
            this.imageUrl = p.getImageUrl();
            this.district = p.getDistrict();
            this.taluka = p.getTaluka();
            this.wardNo = p.getWardNo();
        }
    }
}
