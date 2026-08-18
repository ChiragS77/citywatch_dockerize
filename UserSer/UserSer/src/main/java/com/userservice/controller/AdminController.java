package com.userservice.controller;

import com.userservice.dto.*;
import com.userservice.entity.*;
import com.userservice.repo.ActivityLogRepository;
import com.userservice.service.ActivityLogService;
import com.userservice.service.AdminAlertService;
import com.userservice.service.AdminHealthService;
import com.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ActivityLogRepository   activityLogRepository;
    private final ActivityLogService activityLogService;
    private final AdminAlertService  adminAlertService;

    private final AdminHealthService adminHealthService;

    private final AdminService adminService;

    // Create nagarsevak
//    @PostMapping("/create-nagarsevak")
//    public User createNagarsevak(@ModelAttribute CreatePoliticalUserRequest request) {
//        return adminService.createPoliticalUser(request, Role.NAGARSEVAK);
//    }

//    //create Nagaradhayaksha
//    @PostMapping("/create-nagaradhyaksha")
//    public User createNagaradhyaksha(@ModelAttribute CreatePoliticalUserRequest request) {
//        return adminService.createPoliticalUser(request, Role.NAGARADHYAKSHA);
//    }

//    //view nagarsevak
//    @GetMapping("/nagarsevaks")
//    public List<User> getNagarsevaks() {
//        return adminService.getUsersByRole(Role.NAGARSEVAK);
//    }

    //view nagaradhyaksha
//    @GetMapping("/nagaradhyaksha")
//    public List<User> getNagaradhyaksha() {
//        return adminService.getUsersByRole(Role.NAGARADHYAKSHA);
//    }

    //update nagarsevk
    @PutMapping("/political-user/{id}")
    public User updatePoliticalUser(
            @PathVariable Long id,
            @ModelAttribute CreatePoliticalUserRequest request
    ) {
        return adminService.updatePoliticalUser(id, request);
    }

    //remove nagarsevak/nagaradhyaksha
    @DeleteMapping("/political-user/{id}")
    public ResponseEntity<?> deletePoliticalUser(@PathVariable Long id) {
        adminService.deletePoliticalUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

//    // - get workers---
//    @GetMapping("/workers")
//    public List<Worker> getWorkers() {
//        return adminService.getAllWorkers();
//    }

//    @PutMapping("/worker/{id}/deactivate")
//    public ResponseEntity<?> deactivateWorker(@PathVariable Long id) {
//        adminService.deactivateWorker(id);
//        return ResponseEntity.ok("Worker deactivated successfully");
//    }


    @GetMapping("/system-health")
    public ResponseEntity<List<ServiceHealthResponse>> getSystemHealth() {
        return ResponseEntity.ok(adminHealthService.checkAllServices());
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<AdminAlertResponse>> getAlerts() {
        return ResponseEntity.ok(adminAlertService.getAlerts());
    }

    @GetMapping("/stats")
    public DashboardStatsDto getStats() {
        return adminService.getStats();
    }

    @GetMapping("/citizen-growth")
    public ResponseEntity<List<UserGrowthDTO>> getCitizenGrowth() {
        return ResponseEntity.ok(adminService.getCitizenGrowthLastSixMonths());
    }


    @GetMapping("/ward-distribution")
    public ResponseEntity<List<WardDistributionDTO>> getWardDistribution() {
        return ResponseEntity.ok(adminService.getWardDistribution());
    }




    @GetMapping("/recent-activities")
    public List<ActivityLog> getRecentActivities() {
        return activityLogService.getRecentActivities();
    }


    // Create Nagaradhyaksha
    @PostMapping("/create-nagaradhyaksha")
    public ResponseEntity<User> createNagaradhyaksha(
            @ModelAttribute CreatePoliticalUserRequest request
    ) {
        User user = adminService.createNagaradhyaksha(request);
        return ResponseEntity.ok(user);
    }

    // View all Nagaradhyaksha
    @GetMapping("/nagaradhyaksha")
    public ResponseEntity<List<User>> getAllNagaradhyaksha() {
        return ResponseEntity.ok(adminService.getNagaradhyaksha());
    }

    // View Nagaradhyaksha by location
    @GetMapping("/nagaradhyaksha/location")
    public ResponseEntity<User> getNagaradhyakshaByLocation(
            @RequestParam String state,
            @RequestParam String district,
            @RequestParam String taluka
    ) {
        return ResponseEntity.ok(
                adminService.getNagaradhyakshaByLocation(state, district, taluka)
        );
    }

    // Delete Nagaradhyaksha
    @DeleteMapping("/nagaradhyaksha/{id}")
    public ResponseEntity<String> deleteNagaradhyaksha(@PathVariable Long id) {
        adminService.deleteNagaradhyaksha(id);
        return ResponseEntity.ok("Nagaradhyaksha deleted successfully");
    }


    //_____________________________ NAGARSEVAK ___________________________________
    // Create Nagarsevak
    @PostMapping("/create-nagarsevak")
    public ResponseEntity<User> createNagarsevak(
            @ModelAttribute CreatePoliticalUserRequest request
    ) {
        return ResponseEntity.ok(adminService.createNagarsevak(request));
    }

    // View all Nagarsevaks
    @GetMapping("/nagarsevaks")
    public ResponseEntity<List<User>> getAllNagarsevaks() {
        return ResponseEntity.ok(adminService.getNagarsevaks());
    }

    // View Nagarsevak by ward
    @GetMapping("/nagarsevak/location")
    public ResponseEntity<User> getNagarsevakByWard(
            @RequestParam String state,
            @RequestParam String district,
            @RequestParam String taluka,
            @RequestParam Integer wardNo
    ) {
        return ResponseEntity.ok(
                adminService.getNagarsevakByWard(state, district, taluka, wardNo)
        );
    }

    // Delete Nagarsevak
    @DeleteMapping("/nagarsevak/{id}")
    public ResponseEntity<String> deleteNagarsevak(@PathVariable Long id) {
        adminService.deleteNagarsevak(id);
        return ResponseEntity.ok("Nagarsevak deleted successfully");
    }

//    _____________________________- Worker ENDPOINTS ______________________
    // ===============================
// AdminController.java
// ===============================

    // View all workers
    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return ResponseEntity.ok(adminService.getAllWorkers());
    }

    // View worker by id
    @GetMapping("/worker/{id}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getWorkerById(id));
    }

    // Filter workers by work type
    @GetMapping("/workers/type")
    public ResponseEntity<List<Worker>> getWorkersByType(
            @RequestParam WorkType workType
    ) {
        return ResponseEntity.ok(adminService.getWorkersByType(workType));
    }

    // Filter workers by location
    @GetMapping("/workers/location")
    public ResponseEntity<List<Worker>> getWorkersByLocation(
            @RequestParam String state,
            @RequestParam String district,
            @RequestParam String taluka
    ) {
        return ResponseEntity.ok(
                adminService.getWorkersByLocation(state, district, taluka)
        );
    }

    // Activate worker
    @PutMapping("/worker/{id}/activate")
    public ResponseEntity<String> activateWorker(@PathVariable Long id) {
        adminService.activateWorker(id);
        return ResponseEntity.ok("Worker activated successfully");
    }

    // Deactivate worker
    @PutMapping("/worker/{id}/deactivate")
    public ResponseEntity<String> deactivateWorker(@PathVariable Long id) {
        adminService.deactivateWorker(id);
        return ResponseEntity.ok("Worker deactivated successfully");
    }

    // Delete worker account
    @DeleteMapping("/worker/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable Long id) {
        adminService.deleteWorker(id);
        return ResponseEntity.ok("Worker deleted successfully");
    }

}
