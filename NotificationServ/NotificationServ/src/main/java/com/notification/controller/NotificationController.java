package com.notification.controller;

import com.notification.dto.NotificationDto;
import com.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    // 🔥 Get worker notifications
    @GetMapping
    public List<NotificationDto> getNotifications(HttpServletRequest request) {

        String email = (String) request.getAttribute("email");

        return service.getWorkerNotifications(email);
    }

    // 🔥 Accept / Reject
    @PutMapping("/{id}")
    public void updateStatus(@PathVariable Long id,
                             @RequestParam String status) {

        service.updateStatus(id, status);
    }

}
