package com.complaintservice.controller;


import com.complaintservice.dto.NotificationDto;
import com.complaintservice.dto.WorkerNotificationDto;
import com.complaintservice.entity.Notification;
import com.complaintservice.entity.Post;
import com.complaintservice.entity.Status;
import com.complaintservice.repository.PostRepository;
import com.complaintservice.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationService service;
    private final PostRepository repo;

    @GetMapping
    public List<WorkerNotificationDto> getNotifications(HttpServletRequest request) {
        String email = (String) request.getAttribute("username");
        return service.getWorkerNotifications(email);
    }

    @GetMapping("/nagarsevak")
    public List<Notification> getNagarsevakNotifications(HttpServletRequest request) {
        Integer wardNo = (Integer) request.getAttribute("wardNo");
        return service.getNagarsevakNotifications(wardNo);
    }

    @PostMapping("/{id}/create-order")
    public ResponseEntity<?> createOrder(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(service.createRazorpayOrder(id));
    }

    @PutMapping("/{id}/payment-success")
    public ResponseEntity<?> paymentSuccess(@PathVariable Long id) {
        service.markPaymentSuccess(id);
        return ResponseEntity.ok("Payment marked as paid");
    }

    @PutMapping("/{id}")
    public void updateStatus(@PathVariable Long id,
                             @RequestParam String status) {
        service.updateStatus(id, status);
    }


}
