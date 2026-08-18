package com.complaintservice.service;

import com.complaintservice.entity.Assignment;
import com.complaintservice.entity.AssignmentStatus;
import com.complaintservice.entity.Post;
import com.complaintservice.entity.Status;
import com.complaintservice.repository.AssignmentRepository;
import com.complaintservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository repo;
    private final PostRepository postRepo;   // ✅ ADD THIS
    private final NotificationService notificationService;

    public void assignWorker(Long complaintId, String workerEmail) {

        // ✅ FETCH POST CORRECTLY
        Post complaint = postRepo.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // ✅ UPDATE STATUS
        complaint.setStatus(Status.ACCEPTED); // or ASSIGNED (if you add enum)
        postRepo.save(complaint);

        // ✅ CREATE ASSIGNMENT ENTRY
        Assignment assignment = new Assignment();
        assignment.setPostId(complaintId);
        assignment.setStatus(AssignmentStatus.PENDING);
        assignment.setWorkerEmail(workerEmail);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setExpiresAt(LocalDateTime.now().plusMinutes(10)); // auto expire

        repo.save(assignment);

        // ✅ SEND NOTIFICATION
        notificationService.sendNotification(
                workerEmail,
                complaintId,
                "New complaint assigned to you"
        );
    }
}
