package com.complaintservice.config;

import com.complaintservice.entity.Assignment;
import com.complaintservice.entity.AssignmentStatus;
import com.complaintservice.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssignmentScheduler {

    private final AssignmentRepository repo;

    @Scheduled(fixedRate = 60000) // every 1 min
    public void expireAssignments() {

        List<Assignment> list = repo.findByStatus(AssignmentStatus.PENDING);

        for (Assignment a : list) {
            if (a.getExpiresAt().isBefore(LocalDateTime.now())) {
                a.setStatus(AssignmentStatus.EXPIRED);
                repo.save(a);
            }
        }
    }
}
