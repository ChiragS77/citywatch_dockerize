package com.complaintservice.service;

import com.complaintservice.dto.CommentResponse;
import com.complaintservice.entity.Comment;
import com.complaintservice.entity.Post;
import com.complaintservice.repository.CommentRepository;
import com.complaintservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentSerivce {

    private final CommentRepository repo;
    private final PostRepository postRepo;

    public CommentResponse addComment(Long postId, String text, String username) {

        if (username == null) {
            throw new RuntimeException("Unauthorized");
        }

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Comment cannot be empty");
        }

        if (text.length() > 500) {
            throw new RuntimeException("Comment too long");
        }

        Comment c = new Comment();
        c.setPostId(postId);
        c.setText(text.trim());
        c.setUsername(username);

        Comment saved = repo.save(c);

        CommentResponse res = new CommentResponse();
        res.setId(saved.getId());
        res.setText(saved.getText());
        res.setUsername(saved.getUsername());
        res.setCreatedAt(saved.getCreatedAt());

        return res;
    }

    public List<CommentResponse> getComments(Long postId) {

        return repo.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(c -> {
                    CommentResponse res = new CommentResponse();
                    res.setId(c.getId());
                    res.setText(c.getText());
                    res.setUsername(c.getUsername());
                    res.setCreatedAt(c.getCreatedAt());
                    return res;
                })
                .toList();
    }


}
