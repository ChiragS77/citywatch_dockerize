package com.userservice.repo;

import com.userservice.entity.WorkType;
import com.userservice.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {


    Optional<Worker> findByUserEmail(String email);


    List<Worker> findByWorkTypeAndUser_DistrictAndUser_TalukaAndAvailableTrue(
            WorkType workType,
            String district,
            String taluka
    );


    boolean existsByUserEmail(String email);


    List<Worker> findByWorkType(WorkType workType);

    List<Worker> findByAvailable(Boolean available);

    List<Worker> findByUser_StateIgnoreCaseAndUser_DistrictIgnoreCaseAndUser_TalukaIgnoreCase(
            String state,
            String district,
            String taluka
    );

}
