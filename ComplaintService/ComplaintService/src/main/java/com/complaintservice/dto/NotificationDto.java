package com.complaintservice.dto;

import com.complaintservice.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {


    private Long id;
    private String message;
    private Long complaintId;
    private String status;

    public NotificationDto(Notification n) {
        this.id = n.getId();
        this.message = n.getMessage();
        this.complaintId = n.getComplaintId();
        this.status = n.getStatus().name();
    }

}
