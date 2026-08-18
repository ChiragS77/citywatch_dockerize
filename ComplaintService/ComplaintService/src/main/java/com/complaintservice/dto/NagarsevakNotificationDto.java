package com.complaintservice.dto;

import com.complaintservice.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NagarsevakNotificationDto {

    private Long id;
    private Long complaintId;
    private String message;
    private String status;

    private String proofImageUrl;
    private Double amount;

    public NagarsevakNotificationDto(Notification n) {
        this.id = n.getId();
        this.complaintId = n.getComplaintId();
        this.message = n.getMessage();
        this.status = n.getStatus().name();
        this.proofImageUrl = n.getProofImageUrl();
        this.amount = n.getAmount();
    }
}
