package com.complaintservice.repository;

import com.complaintservice.entity.Assignment;
import com.complaintservice.entity.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByStatus(AssignmentStatus status);
}
