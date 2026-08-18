package com.notification.service;

import com.notification.dto.NotificationDto;
import com.notification.entity.Notification;
import com.notification.entity.Status;
import com.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 🔥 Create + Send Notification
    public void sendNotification(String workerEmail, Long complaintId, String message) {

        Notification n = new Notification();
        n.setWorkerEmail(workerEmail);
        n.setComplaintId(complaintId);
        n.setMessage(message);
        n.setStatus(Status.PENDING);

        repo.save(n);

        // 🔥 WebSocket push
        messagingTemplate.convertAndSendToUser(
                workerEmail,
                "/queue/notifications",
                new NotificationDto(n)
        );
    }

    // 🔥 Worker Action
    public void updateStatus(Long notificationId, String status) {

        Notification n = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setStatus(Status.valueOf(status));
        n.setActionTakenAt(LocalDateTime.now());

        repo.save(n);
    }

    // 🔥 Fetch notifications
    public List<NotificationDto> getWorkerNotifications(String email) {
        return repo.findByWorkerEmailAndIsDeletedFalse(email)
                .stream()
                .map(NotificationDto::new)
                .toList();
    }

}
