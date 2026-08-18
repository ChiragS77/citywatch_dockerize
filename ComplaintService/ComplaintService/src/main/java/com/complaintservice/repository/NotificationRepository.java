package com.complaintservice.repository;

import com.complaintservice.entity.Notification;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByWorkerEmailAndIsDeletedFalse(String email);

    List<Notification> findByActionTakenAtBefore(LocalDateTime time);

    List<Notification> findByIsDeletedFalseAndCreatedAtBefore(LocalDateTime time);

    @Modifying
    @Query("""
UPDATE Notification n
SET n.isDeleted = true
WHERE n.isDeleted = false
AND n.createdAt < :time
""")
    void softDeleteOldNotifications(@Param("time") LocalDateTime time);


    Optional<Notification>
    findByComplaintIdAndWorkerEmail(
            Long complaintId,
            String workerEmail
    );

    List<Notification> findByWardNoAndIsDeletedFalseOrderByCreatedAtDesc(Integer wardNo);

}
