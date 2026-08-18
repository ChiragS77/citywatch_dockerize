package com.complaintservice.repository;

import com.complaintservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository  extends JpaRepository<Like,Long> {

    Optional<Like> findByPostIdAndUsername(Long postId, String username);

    Long countByPostId(Long postId);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.postId = :postId AND l.username = :username")
    void deleteByPostIdAndUsername(Long postId, String username);

    Boolean existsByPostIdAndUsername(Long postId, String username);
}
