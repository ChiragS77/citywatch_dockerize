package com.userservice.repo;

import com.userservice.entity.PoliticalProfile;
import com.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoliticalPofileRepository extends JpaRepository<PoliticalProfile, Long> {

    Optional<PoliticalProfile> findByUserId(Long userId);


    Optional<PoliticalProfile> findByUser(User user);



    void deleteByUser(User user);


}
