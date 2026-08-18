package com.userservice.service;

import com.userservice.dto.CreatePoliticalUserRequest;
import com.userservice.dto.DashboardStatsDto;
import com.userservice.dto.UserGrowthDTO;
import com.userservice.dto.WardDistributionDTO;
import com.userservice.entity.*;
import com.userservice.repo.ActivityLogRepository;
import com.userservice.repo.PoliticalPofileRepository;
import com.userservice.repo.UserRepository;
import com.userservice.repo.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final UserRepository userRepository;
    private final PoliticalPofileRepository profileRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final WorkerRepository workerRepository;
    private final ActivityLogRepository activityLogRepository;
    private final ActivityLogService activityLogService;

    public User createPoliticalUser(CreatePoliticalUserRequest request, Role role) {


        if (role == Role.NAGARSEVAK && request.getWardNo() == null) {
            throw new RuntimeException("WardNo is required for Nagarsevak");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        user.setState(request.getState());
        user.setDistrict(request.getDistrict());
        user.setTaluka(request.getTaluka());

        if (role == Role.NAGARSEVAK) {
            user.setWardNo(request.getWardNo());
        } else {
            user.setWardNo(null);
        }

        userRepository.save(user);

        String imageUrl = cloudinaryService.uploadImage(request.getPartyImage());

        PoliticalProfile profile = new PoliticalProfile();
        profile.setPartyName(request.getPartyName());
        profile.setPartyImageUrl(imageUrl);
        profile.setUser(user);

        profileRepository.save(profile);

        return user;
    }

// update nagarsevaks.........

    public User updatePoliticalUser(Long id, CreatePoliticalUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setState(request.getState());
        user.setDistrict(request.getDistrict());
        user.setTaluka(request.getTaluka());

        if (user.getRole() == Role.NAGARSEVAK) {
            user.setWardNo(request.getWardNo());
        }

        PoliticalProfile profile = profileRepository.findByUser(user)
                .orElse(new PoliticalProfile());

        profile.setUser(user);
        profile.setPartyName(request.getPartyName());

        if (request.getPartyImage() != null && !request.getPartyImage().isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(request.getPartyImage());
            profile.setPartyImageUrl(imageUrl);
        }

        profileRepository.save(profile);

        return userRepository.save(user);
    }


    public void deletePoliticalUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.NAGARSEVAK &&
                user.getRole() != Role.NAGARADHYAKSHA) {
            throw new RuntimeException("Only political users can be deleted");
        }

        userRepository.delete(user);
    }

//    public List<Worker> getAllWorkers() {
//        return workerRepository.findAll();
//    }
//
//    public void deactivateWorker(Long id) {
//        Worker worker = workerRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Worker not found"));
//
//        worker.setAvailable(false);
//        workerRepository.save(worker);
//    }


    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public DashboardStatsDto getStats() {

        return new DashboardStatsDto(

                userRepository.countByRole(Role.NAGARSEVAK),

                userRepository.countByRole(Role.NAGARADHYAKSHA),

                workerRepository.count(),

                userRepository.countByRole(Role.USER)
        );
    }


    public List<UserGrowthDTO> getCitizenGrowthLastSixMonths() {

        LocalDateTime startDate = LocalDateTime.now()
                .minusMonths(5)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        List<User> users = userRepository
                .findByRoleAndCreatedAtAfterOrderByCreatedAtAsc(Role.USER, startDate);

        Map<YearMonth, Long> grouped = users.stream()
                .collect(Collectors.groupingBy(
                        user -> YearMonth.from(user.getCreatedAt()),
                        TreeMap::new,
                        Collectors.counting()
                ));

        List<UserGrowthDTO> result = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.now().minusMonths(i);

            result.add(new UserGrowthDTO(
                    month.format(formatter),
                    grouped.getOrDefault(month, 0L)
            ));
        }

        return result;
    }


    public List<WardDistributionDTO> getWardDistribution() {

        List<User> citizens = userRepository.findByRole(Role.USER);

        Map<Integer, Long> grouped = citizens.stream()
                .filter(user -> user.getWardNo() != null)
                .collect(Collectors.groupingBy(
                        User::getWardNo,
                        TreeMap::new,
                        Collectors.counting()
                ));

        return grouped.entrySet()
                .stream()
                .map(entry -> new WardDistributionDTO(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
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

    // ___________________ NAGARADHYAKSHA METHODS _________________________

    public User createNagaradhyaksha(CreatePoliticalUserRequest request) {

        boolean exists = userRepository
                .existsByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCase(
                        Role.NAGARADHYAKSHA,
                        request.getState(),
                        request.getDistrict(),
                        request.getTaluka()
                );

        if (exists) {
            throw new RuntimeException(
                    "Nagaradhyaksha already exists for this city. Delete existing Nagaradhyaksha first."
            );
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.NAGARADHYAKSHA);

        user.setState(request.getState());
        user.setDistrict(request.getDistrict());
        user.setTaluka(request.getTaluka());
        user.setWardNo(null);

        userRepository.save(user);

        String imageUrl = null;

        if (request.getPartyImage() != null && !request.getPartyImage().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(request.getPartyImage());
        }

        PoliticalProfile profile = new PoliticalProfile();
        profile.setPartyName(request.getPartyName());
        profile.setPartyImageUrl(imageUrl);
        profile.setUser(user);

        profileRepository.save(profile);

        activityLogService.saveLog(
                "CREATE_NAGARADHYAKSHA",
                "Admin created new Nagaradhyaksha account",
                "Admin"
        );

        return user;
    }

    public List<User> getNagaradhyaksha() {
        return userRepository.findByRole(Role.NAGARADHYAKSHA);
    }

    public User getNagaradhyakshaByLocation(
            String state,
            String district,
            String taluka
    ) {
        return userRepository
                .findByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCase(
                        Role.NAGARADHYAKSHA,
                        state,
                        district,
                        taluka
                )
                .orElseThrow(() -> new RuntimeException(
                        "No Nagaradhyaksha found for this city"
                ));
    }

    @Transactional
    public void deleteNagaradhyaksha(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.NAGARADHYAKSHA) {
            throw new RuntimeException("This user is not Nagaradhyaksha");
        }

        profileRepository.deleteByUser(user);

        userRepository.delete(user);
        activityLogService.saveLog(
                "DELETE_NAGARADHYAKSHA",
                "Admin deleted Nagaradhyaksha: " + user.getName(),
                "Admin"
        );
    }




    @Transactional
    public User createNagarsevak(CreatePoliticalUserRequest request) {

        if (request.getWardNo() == null) {
            throw new RuntimeException("WardNo is required for Nagarsevak");
        }

        boolean exists = userRepository
                .existsByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCaseAndWardNo(
                        Role.NAGARSEVAK,
                        request.getState(),
                        request.getDistrict(),
                        request.getTaluka(),
                        request.getWardNo()
                );

        if (exists) {
            throw new RuntimeException(
                    "Nagarsevak already exists in this ward. Remove existing Nagarsevak first."
            );
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.NAGARSEVAK);

        user.setState(request.getState());
        user.setDistrict(request.getDistrict());
        user.setTaluka(request.getTaluka());
        user.setWardNo(request.getWardNo());

        userRepository.save(user);

        String imageUrl = null;

        if (request.getPartyImage() != null && !request.getPartyImage().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(request.getPartyImage());
        }

        PoliticalProfile profile = new PoliticalProfile();
        profile.setPartyName(request.getPartyName());
        profile.setPartyImageUrl(imageUrl);
        profile.setUser(user);

        profileRepository.save(profile);

        activityLogService.saveLog(
                "CREATE_NAGARSEVAK",
                "Admin created new Nagarsevak account",
                "Admin"
        );

        return user;
    }

    public List<User> getNagarsevaks() {
        return userRepository.findByRole(Role.NAGARSEVAK);
    }

    public User getNagarsevakByWard(
            String state,
            String district,
            String taluka,
            Integer wardNo
    ) {
        return userRepository
                .findByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCaseAndWardNo(
                        Role.NAGARSEVAK,
                        state,
                        district,
                        taluka,
                        wardNo
                )
                .orElseThrow(() -> new RuntimeException(
                        "No Nagarsevak found for this ward"
                ));
    }

    @Transactional
    public void deleteNagarsevak(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.NAGARSEVAK) {
            throw new RuntimeException("This user is not Nagarsevak");
        }

        profileRepository.findByUser(user)
                .ifPresent(profileRepository::delete);

        userRepository.delete(user);

        activityLogService.saveLog(
                "DELETE_NAGARASEVAK",
                "Admin deleted Nagarsevak: " + user.getName(),
                "Admin"
        );
    }


//    ____________________ WORKER SERVICE METHODS______________________________

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    public List<Worker> getWorkersByType(WorkType workType) {
        return workerRepository.findByWorkType(workType);
    }

    public List<Worker> getWorkersByLocation(
            String state,
            String district,
            String taluka
    ) {
        return workerRepository
                .findByUser_StateIgnoreCaseAndUser_DistrictIgnoreCaseAndUser_TalukaIgnoreCase(
                        state,
                        district,
                        taluka
                );
    }

    @Transactional
    public void activateWorker(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        worker.setAvailable(true);
        workerRepository.save(worker);
    }

    @Transactional
    public void deactivateWorker(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        worker.setAvailable(false);
        workerRepository.save(worker);
    }

    @Transactional
    public void deleteWorker(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        User user = worker.getUser();

        workerRepository.delete(worker);
        activityLogService.saveLog(
                "DELETE_WORKER",
                "Admin deleted worker: " + user.getName(),
                "Admin"
        );

        if (user != null) {
            userRepository.delete(user);
        }
    }

}
