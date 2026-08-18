package com.userservice.service;

import com.userservice.entity.ActivityLog;
import com.userservice.repo.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {


    private final ActivityLogRepository activityLogRepository;

    public List<ActivityLog> getRecentActivities() {

        return activityLogRepository.findAll(
                PageRequest.of(
                        0,
                        10,
                        Sort.by("createdAt").descending()
                )
        ).getContent();
    }

    public void logActivity(
            String action,
            String performedBy,
            String details
    ) {

        ActivityLog log = new ActivityLog();

        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setDetails(details);
        log.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(log);
    }

    public void saveLog(String action, String details, String performedBy) {
        ActivityLog log = new ActivityLog();
        log.setAction(action);
        log.setDetails(details);
        log.setPerformedBy(performedBy);
        log.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(log);
    }
}
