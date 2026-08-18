package com.complaintservice.controller;

import com.complaintservice.entity.Assignment;
import com.complaintservice.entity.AssignmentStatus;
import com.complaintservice.entity.Post;
import com.complaintservice.entity.Status;
import com.complaintservice.repository.AssignmentRepository;
import com.complaintservice.repository.PostRepository;
import com.complaintservice.service.AssignmentService;
import com.complaintservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {


    private final AssignmentService service;
private final NotificationService notificationService;
private final AssignmentRepository repo;

    private final PostRepository postRepo;

    public Assignment acceptAssignment(Long id) {

        Assignment a = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (a.getExpiresAt().isBefore(LocalDateTime.now())) {
            a.setStatus(AssignmentStatus.EXPIRED);
            return repo.save(a);
        }

        a.setStatus(AssignmentStatus.ACCEPTED);

        // ✅ UPDATE POST STATUS
        Post post = postRepo.findById(a.getPostId())
                .orElseThrow();

        post.setStatus(Status.ACCEPTED);
        postRepo.save(post);

        return repo.save(a);
    }


//    @PutMapping("/{id}/assign")
//    public void assignWorker(
//            @PathVariable Long id,
//            @RequestParam String workerEmail
//    ) {
//        service.assignWorker(id, workerEmail);
//    }

    @PutMapping("/{id}/assign")
    public void assignWorker(
            @PathVariable Long id,
            @RequestParam String worker
    ) {
        service.assignWorker(id, worker);
    }


}
