package com.complaintservice.service;

import com.complaintservice.dto.LikeResponse;
import com.complaintservice.entity.Like;
import com.complaintservice.entity.Post;
import com.complaintservice.repository.LikeRepository;
import com.complaintservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository repo;
    private final PostRepository postRepo;


    public Map<String, Object> toggleLike(Long postId, String username) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Like> existing =
                repo.findByPostIdAndUsername(postId, username);

        String status;

        if (existing.isPresent()) {
            repo.delete(existing.get());
            status = "UNLIKED";
        } else {
            Like like = new Like();
            like.setPostId(postId);
            like.setUsername(username);
            repo.save(like);
            status = "LIKED";
        }

        long likeCount = repo.countByPostId(postId);

        return Map.of(
                "status", status,
                "likeCount", likeCount
        );
    }

    public long getLikeCount(Long postId) {
        return repo.countByPostId(postId);
    }
}