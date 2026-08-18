package com.complaintservice.config;

import com.complaintservice.entity.Notification;
import com.complaintservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@RequiredArgsConstructor
@Component
public class NotificationScheduler {

    private final NotificationRepository repo;

    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteOldNotifications() {

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        List<Notification> list =
                repo.findByIsDeletedFalseAndCreatedAtBefore(oneMonthAgo);

        for (Notification n : list) {
            n.setIsDeleted(true);
        }

        repo.saveAll(list);
    }
}
