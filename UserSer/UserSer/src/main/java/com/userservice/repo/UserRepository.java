package com.userservice.repo;

import com.userservice.dto.UserGrowthDTO;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    long countByRole(Role role);


    Optional<User> findByEmail(String email);

    Optional<User> findByRoleAndWardNoAndTalukaAndDistrict(
            Role role,
            Integer wardNo,
            String taluka,
            String district
    );

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);


    List<User> findByWardNoAndTalukaAndDistrict(
            Integer wardNo,
            String taluka,
            String district
    );

    List<User> findByRoleAndWardNo(Role role, Integer wardNo);


    List<User> findByRoleAndCreatedAtAfterOrderByCreatedAtAsc(
            Role role,
            LocalDateTime startDate
    );



    boolean existsByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCase(
            Role role,
            String state,
            String district,
            String taluka
    );

    Optional<User> findByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCase(
            Role role,
            String state,
            String district,
            String taluka
    );



    boolean existsByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCaseAndWardNo(
            Role role,
            String state,
            String district,
            String taluka,
            Integer wardNo
    );

    Optional<User> findByRoleAndStateIgnoreCaseAndDistrictIgnoreCaseAndTalukaIgnoreCaseAndWardNo(
            Role role,
            String state,
            String district,
            String taluka,
            Integer wardNo
    );



}
