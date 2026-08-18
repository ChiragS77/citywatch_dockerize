package com.userservice.service;

import com.userservice.dto.WorkerDTO;
import com.userservice.dto.WorkerResponse;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.entity.WorkType;
import com.userservice.entity.Worker;
import com.userservice.repo.UserRepository;
import com.userservice.repo.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository repo;
    private final UserRepository userRepo;
    private final CloudinaryService cloudinaryService;

//    public Worker registerWorker(String email, WorkType type, MultipartFile image) {
//
//        User user = userRepo.findByEmail(email).orElseThrow();
//
//        String imageUrl = cloudinaryService.uploadImage(image);
//
//        Worker worker = new Worker();
//        worker.setUser(user);
//        worker.setWorkType(type);
//        worker.setProfileImage(imageUrl);
//
//        return repo.save(worker);
//    }

    public Worker registerWorker(
            String email,
            WorkType type,
            MultipartFile image
    ) {

        if(repo.existsByUserEmail(email)) {
            throw new RuntimeException("Worker already registered");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String imageUrl = cloudinaryService.uploadImage(image);

        Worker worker = new Worker();

        worker.setUser(user);

        worker.setWorkType(type);

        worker.setProfileImage(imageUrl);

        worker.setAvailable(true);

        worker.setRating(0.0);

        return repo.save(worker);
    }

    public List<WorkerResponse> getWorkers(WorkType type, String district, String taluka) {

        List<Worker> workers =
                repo.findByWorkTypeAndUser_DistrictAndUser_TalukaAndAvailableTrue(
                        type, district, taluka
                );

        return workers.stream().map(w -> {

            WorkerResponse res = new WorkerResponse();

            res.setId(w.getId());
            res.setName(w.getUser().getName());
            res.setEmail(w.getUser().getEmail());

            res.setWorkType(w.getWorkType().name());
            res.setExperience(w.getExperience());
            res.setRating(w.getRating());
            res.setAvailable(w.getAvailable());

            res.setProfileImage(w.getProfileImage());

            res.setDistrict(w.getUser().getDistrict());
            res.setTaluka(w.getUser().getTaluka());

            return res;

        }).toList();
    }

    // ===================== WorkerService =====================
    public WorkerResponse updateWorkerInfo(
            String email,
            String name,
            String district,
            String taluka,
            String experience,
            Boolean available,
            String workType,
            String upiId
    ) {

        System.out.println("Inside a method....");

        Worker worker = repo.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        // 🔹 UPDATE USER DATA
        User user = worker.getUser();
        if (user != null) {
            user.setName(name);
            user.setDistrict(district);
            user.setTaluka(taluka);
        }


        // 🔹 UPDATE WORKER DATA
        worker.setExperience(experience);
        worker.setAvailable(available);
        worker.setUpiId(upiId); //worker upi id

        // 🔥 SAFE ENUM
        if (workType != null && !workType.isBlank()) {
            try {
                worker.setWorkType(
                        WorkType.valueOf(workType.trim().toUpperCase())
                );
            } catch (Exception e) {
                System.out.println("Invalid WorkType: " + workType);
            }
        }

        repo.save(worker);

        return new WorkerResponse(worker);
    }


    // 🔥 IMAGE UPLOAD METHOD (SEPARATE)
    public WorkerResponse updateWorkerImage(String email, MultipartFile image) {

        Worker worker = repo.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        if (image != null && !image.isEmpty()) {

            System.out.println("Uploading image...");

            String imageUrl = cloudinaryService.uploadImage(image);

            worker.setProfileImage(imageUrl);
            repo.save(worker);
        }

        return new WorkerResponse(worker);
    }


    public WorkerResponse getWorkerProfile(String email) {

        Worker worker = repo.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        WorkerResponse res = new WorkerResponse();

        res.setId(worker.getId());

        // 🔹 USER DATA (safe)
        if (worker.getUser() != null) {
            res.setName(worker.getUser().getName());
            res.setEmail(worker.getUser().getEmail());
            res.setDistrict(worker.getUser().getDistrict());
            res.setTaluka(worker.getUser().getTaluka());
            res.setExperience(worker.getExperience());

            res.setUpiId(worker.getUpiId());

            res.setQrImageUrl(worker.getQrImageUrl());
        }

        // 🔹 WORKER DATA (safe)
        res.setWorkType(
                worker.getWorkType() != null
                        ? worker.getWorkType().name()
                        : "NOT_SPECIFIED"
        );

        res.setExperience(
                worker.getExperience() != null
                        ? worker.getExperience()
                        : null
        );

        res.setRating(
                worker.getRating() != null
                        ? worker.getRating()
                        : 0.0
        );

        res.setAvailable(
                worker.getAvailable() != null
                        ? worker.getAvailable()
                        : false
        );

        res.setProfileImage(
                 worker.getProfileImage()

        );

        return res;
    }

    public WorkerDTO getWorkerByEmail(String email) {

        Worker worker =
                repo.findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("Worker not found"));

        User user = worker.getUser();

        WorkerDTO dto = new WorkerDTO();

        dto.setId(worker.getId());

        dto.setName(user.getName());

        dto.setEmail(user.getEmail());

        dto.setProfileImage(worker.getProfileImage());

        dto.setWorkType(worker.getWorkType().name());

        dto.setDistrict(user.getDistrict());

        dto.setTaluka(user.getTaluka());
        dto.setExperience(worker.getExperience());

        dto.setRating(worker.getRating());

        dto.setAvailable(worker.getAvailable());

        dto.setUpiId(worker.getUpiId());

        dto.setQrImageUrl(worker.getQrImageUrl());

        return dto;
    }

}
