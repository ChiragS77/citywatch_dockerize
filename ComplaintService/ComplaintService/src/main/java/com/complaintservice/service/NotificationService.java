package com.complaintservice.service;


import com.complaintservice.client.UserClient;
import com.complaintservice.dto.NagarsevakNotificationDto;
import com.complaintservice.dto.NotificationDto;
import com.complaintservice.dto.WorkerNotificationDto;
import com.complaintservice.entity.Notification;
import com.complaintservice.entity.PaymentStatus;
import com.complaintservice.entity.Post;
import com.complaintservice.entity.Status;
import com.complaintservice.repository.NotificationRepository;
import com.complaintservice.repository.PostRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private UserClient userClient;

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;


    @Autowired
    private NotificationRepository repo;

    @Autowired
    private PostRepository postRepo;

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

        // 🔥 UPDATE NOTIFICATION
        n.setStatus(Status.valueOf(status));
        n.setActionTakenAt(LocalDateTime.now());
        repo.save(n);

        // 🔥 ALSO UPDATE POST
        Post post = postRepo.findById(n.getComplaintId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (status.equals("ACCEPTED")) {
            post.setStatus(Status.ACCEPTED);
        } else if (status.equals("REJECTED")) {
            post.setStatus(Status.REJECTED);
        }

        postRepo.save(post);
    }

    // 🔥 Fetch notifications
    public List<WorkerNotificationDto> getWorkerNotifications(String email) {

        return repo.findByWorkerEmailAndIsDeletedFalse(email)
                .stream()
                .map(n -> {
                    Post post = postRepo.findById(n.getComplaintId()).orElse(null);
                    return new WorkerNotificationDto(n, post);
                })
                .toList();
    }


    public void notifyNagarsevak(Notification notification) {

        List<String> emails =
                userClient.getNagarsevakEmails(notification.getWardNo());

        for (String email : emails) {

            messagingTemplate.convertAndSendToUser(
                    email,
                    "/queue/notifications",
                    new NagarsevakNotificationDto(notification)
            );
        }


    }

    public void markPaymentSuccess(Long notificationId) {

        Notification n = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setPaymentDone(true);
        n.setPaymentStatus(PaymentStatus.PAID);
        n.setStatus(Status.COMPLETED);
        n.setActionTakenAt(LocalDateTime.now());
        repo.save(n);

        Post post = postRepo.findById(n.getComplaintId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setStatus(Status.COMPLETED);
        postRepo.save(post);
    }

    public List<Notification> getNagarsevakNotifications(Integer wardNo) {
        return repo.findByWardNoAndIsDeletedFalseOrderByCreatedAtDesc(wardNo);
    }

    public Map<String, Object> createRazorpayOrder(Long notificationId) throws Exception {

        Notification n = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", n.getAmount().intValue() * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "notif_" + n.getId());

        Order    order = razorpay.orders.create(orderRequest);

        return Map.of(
                "key", keyId,
                "amount", order.get("amount"),
                "currency", order.get("currency"),
                "orderId", order.get("id"),
                "notificationId", n.getId()
        );
    }

}
