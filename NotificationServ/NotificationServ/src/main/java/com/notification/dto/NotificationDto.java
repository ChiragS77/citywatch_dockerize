package com.notification.dto;

import com.notification.entity.Notification;

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
