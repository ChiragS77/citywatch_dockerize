package com.userservice.controller;

import com.userservice.dto.WorkerDTO;
import com.userservice.dto.WorkerResponse;
import com.userservice.entity.WorkType;
import com.userservice.entity.Worker;
import com.userservice.service.WorkerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerWorker(
            @RequestParam("workType") WorkType workType,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request
    ) {

        String email = (String) request.getAttribute("username");

        Worker worker = workerService.registerWorker(email, workType, image);

        return ResponseEntity.ok(worker);
    }

    @GetMapping
    public List<WorkerResponse> getWorkers(
            @RequestParam String type,
            HttpServletRequest request) {

        System.out.println("================= Enter into getWorkers  ===================");
        String district = (String) request.getAttribute("district");
        String taluka = (String) request.getAttribute("taluka");


        WorkType workType;
            System.out.println("========================"+type);
        try {
            workType = WorkType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid WorkType: " + type);
        }

        return workerService.getWorkers(workType, district, taluka);
    }

    @PutMapping("/update-info")
    public WorkerResponse updateInfo(
            @RequestParam String name,
            @RequestParam String district,
            @RequestParam String taluka,
            @RequestParam String experience,
            @RequestParam Boolean available,
            @RequestParam String workType,
            @RequestParam String upiId,
            Authentication auth
    ) {
        return workerService.updateWorkerInfo(
                auth.getName(), name, district, taluka, experience, available, workType, upiId
        );
    }

    // 🔥 2. UPLOAD IMAGE (SEPARATE SLOW API)
    @PostMapping("/upload-image")
    public WorkerResponse uploadImage(
            @RequestParam MultipartFile image,
            Authentication auth
    ) {
        return workerService.updateWorkerImage(auth.getName(), image);
    }

    @GetMapping("/me")
    public WorkerResponse getWorker(HttpServletRequest request) {
        String email = (String) request.getAttribute("username");
        return this.workerService.getWorkerProfile(email);
    }

    @GetMapping("/email")
    public WorkerDTO getWorkerByEmail(
            @RequestParam String email) {

        return workerService.getWorkerByEmail(email);
    }
}
