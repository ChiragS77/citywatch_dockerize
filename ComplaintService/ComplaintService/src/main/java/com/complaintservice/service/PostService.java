package com.complaintservice.service;


import com.complaintservice.client.UserClient;
import com.complaintservice.dto.PageResponse;
import com.complaintservice.dto.PostResponse;
import com.complaintservice.dto.WorkerDTO;
import com.complaintservice.entity.*;
import com.complaintservice.repository.CommentRepository;
import com.complaintservice.repository.LikeRepository;
import com.complaintservice.repository.NotificationRepository;
import com.complaintservice.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository repo;
    private final LikeRepository likeRepo;
    private final CommentRepository commentRepo;
    private final ImageService imageService;
    private final NotificationRepository notificationRepo;
    private final UserClient    userClient;

    private final NotificationService  notificationService;


    @CacheEvict(value = "feedCache", allEntries = true)
    public Post createPost(Post post, String username, Integer wardNo,
                           String district, String taluka) {

        post.setUsername(username.trim().toLowerCase());
        post.setWardNo(wardNo);
        post.setDistrict(district);
        post.setTaluka(taluka);

        if (post.getType() == PostType.COMPLAINT) {
            post.setStatus(Status.PENDING);

            if (post.getWorkType() == null) {
                throw new RuntimeException("WorkType is required for complaint");
            }
        } else {
            post.setStatus(null);
        }



        return repo.save(post);
    }

//    public List<Post> getWardFeed(Integer wardNo) {
//        return repo.findByWardNoOrderByCreatedAtDesc(wardNo);
//    }


//    @CacheEvict(value = {"statsCache", "complaintsCache"}, allEntries = true)
    public Post updateStatus(Long postId,
                             Status status,
                             String role,
                             Integer wardNo,
                             String username) {

        // 🔐 Only Nagarsevak
        if (!"NAGARSEVAK".equals(role)) {
            throw new RuntimeException("Only Nagarsevak allowed");
        }

        Post post = repo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Only complaint
        if (!"COMPLAINT".equals(post.getType())) {
            throw new RuntimeException("Not a complaint");
        }

        // Same ward
        if (!post.getWardNo().equals(wardNo)) {
            throw new RuntimeException("Unauthorized");
        }

        // Optional: prevent re-update
        if (post.getStatus() == Status.COMPLETED) {
            throw new RuntimeException("Already completed");
        }

        // ✅ Update status
        post.setStatus(status);

        // 🔥 NEW: audit fields
        post.setUpdatedBy(username);
        post.setUpdatedAt(LocalDateTime.now());

        return repo.save(post);
    }

//    @Cacheable(
//            value = "feedCache",
//            key = "#role + '-' + #district + '-' + #taluka + '-' + #wardNo + '-' + #page + '-' + #size"
//    )
    public List<PostResponse> getFeed(Integer wardNo,
                                      String username,
                                      String role,
                                      String taluka,
                                      String district,
                                      int page,
                                      int size) {

        if (size > 20) size = 20;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Post> posts;

        // 🎯 ROLE BASED LOGIC
        if ("NAGARSEVAK".equals(role)) {

            posts = repo.findByDistrictAndTalukaAndWardNo(
                    district, taluka, wardNo, pageable
            );

        } else {

            posts = repo.findByDistrictAndTaluka(
                    district, taluka, pageable
            );
        }

        // ✅ return LIST (not Page)
        return posts.getContent().stream().map(post -> {

            PostResponse res = new PostResponse();

            res.setId(post.getId());
            res.setCaption(post.getCaption());
            res.setImageUrl(post.getImageUrl());
//            res.setType(post.getType().name());
//            res.setStatus(post.getStatus().name());
            res.setWorkType(
                    post.getWorkType() != null ? post.getWorkType().name() : null
            );
            res.setUsername(post.getUsername());
            res.setCreatedAt(post.getCreatedAt());

            res.setLikeCount(likeRepo.countByPostId(post.getId()));
            res.setCommentCount(commentRepo.countByPostId(post.getId()));

            res.setIsLiked(
                    likeRepo.existsByPostIdAndUsername(post.getId(), username)
            );

            return res;

        }).toList();
    }

//@Cacheable(
//        value = "feedCache",
//        key = "#role + '-' + #district + '-' + #taluka + '-' + #wardNo"
//)
public List<PostResponse> getFeed(Integer wardNo,
                                  String username,
                                  String role,
                                  String taluka,
                                  String district) {

    List<Post> posts;

    if ("NAGARSEVAK".equals(role)) {
        posts = repo.findByDistrictAndTalukaAndWardNoOrderByCreatedAtDesc(
                district, taluka, wardNo
        );
    } else {
        posts = repo.findByDistrictAndTalukaOrderByCreatedAtDesc(
                district, taluka
        );
    }

    return posts.stream().map(post -> {

        PostResponse res = new PostResponse();

        res.setId(post.getId());
        res.setCaption(post.getCaption());
        res.setImageUrl(post.getImageUrl());

        res.setType(post.getType() != null ? post.getType().name() : null);
        res.setStatus(post.getStatus() != null ? post.getStatus().name() : null);

        res.setWorkType(
                post.getWorkType() != null ? post.getWorkType().name() : null
        );

        res.setUsername(post.getUsername());
        res.setCreatedAt(post.getCreatedAt());

        res.setLikeCount(likeRepo.countByPostId(post.getId()));
        res.setCommentCount(commentRepo.countByPostId(post.getId()));

        res.setIsLiked(
                likeRepo.existsByPostIdAndUsername(post.getId(), username)
        );

        return res;

    }).toList();
}

    //    @Cacheable(
//            value = "userPostsCache",
//            key = "#username + '-' + #page"
//    )
    public Page<PostResponse> getUserPosts(String username, int page, int size) {
        System.out.println("========= Enter into posts ============");

        if (size > 20) size = 20;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Post> posts = repo.findByUsernameOrderByCreatedAtDesc(username, pageable);
        System.out.println("========= Exits into posts ============");

        return posts.map(post -> {

            PostResponse res = new PostResponse();

            res.setId(post.getId());
            res.setCaption(post.getCaption());
            res.setImageUrl(post.getImageUrl());
            res.setType(post.getType().name());
            res.setStatus(
                    post.getStatus() != null
                            ? post.getStatus().name()
                            : null
            );
            res.setUsername(post.getUsername());
            System.out.println("--------------- "+post.getUsername());
            res.setCreatedAt(post.getCreatedAt());

            res.setLikeCount(likeRepo.countByPostId(post.getId()));
            res.setCommentCount(commentRepo.countByPostId(post.getId()));

            res.setIsLiked(
                    likeRepo.existsByPostIdAndUsername(post.getId(), username)
            );

            return res;
        });
    }


//    @Cacheable(value = "statsCache", key = "#wardNo + '-' + #taluka + '-' + #district")
    public Map<String, Long> getStats(Integer wardNo) {

        long total = repo.countByWardNoAndType(wardNo, PostType.COMPLAINT);

        long resolved = repo.countByWardNoAndStatus(wardNo, Status.COMPLETED);

        long pending = repo.countByWardNoAndStatus(wardNo, Status.PENDING);

        return Map.of(
                "total", total,
                "resolved", resolved,
                "pending", pending
        );
    }


    public List<Post> getComplaintsByLocation(
            Integer wardNo,
            String taluka,
            String district
    ) {
        return repo.findByWardNoAndTalukaAndDistrictAndTypeOrderByCreatedAtDesc(
                wardNo,
                taluka,
                district,
                PostType.POST
        );
    }


    public Post updateStatus(Long id,
                             Status status,
                             String role,
                             Integer wardNo,
                             String taluka,
                             String district) {

        // 🔐 Only Nagarsevak allowed
        if (!"NAGARSEVAK".equals(role)) {
            throw new RuntimeException("Unauthorized");
        }

        Post post = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // ✅ Ensure same area
        if (!post.getWardNo().equals(wardNo) ||
                !post.getTaluka().equals(taluka) ||
                !post.getDistrict().equals(district)) {
            throw new RuntimeException("Access denied");
        }

        // ✅ Only complaints
        if (post.getType() != PostType.COMPLAINT) {
            throw new RuntimeException("Not a complaint");
        }

        // 🔥 STATUS FLOW CONTROL
        if (post.getStatus() == Status.COMPLETED) {
            throw new RuntimeException("Already completed");
        }

        if (status == Status.IN_PROGRESS && post.getStatus() != Status.PENDING) {
            throw new RuntimeException("Invalid transition");
        }

        if (status == Status.COMPLETED && post.getStatus() == Status.PENDING) {
            throw new RuntimeException("Start work first");
        }

        // ✅ Update
        post.setStatus(status);

        return repo.save(post);
    }

//    @Cacheable(
//            value = "complaints",
//            key = "#wardNo + '-' + #taluka + '-' + #district + '-' + (#status != null ? #status.name() : 'ALL') + '-' + #page + '-' + #size"
//    )
    public PageResponse<PostResponse> getComplaintsByLocation(
            Integer wardNo,
            String taluka,
            String district,
            Status status,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Post> posts;

        if (status != null) {
            posts = repo.findByWardNoAndTalukaAndDistrictAndStatusOrderByCreatedAtDesc(
                    wardNo, taluka, district, status, pageable
            );
        } else {
            posts = repo.findByWardNoAndTalukaAndDistrictOrderByCreatedAtDesc(
                    wardNo, taluka, district, pageable
            );
        }

        List<PostResponse> content = posts
                .map(PostResponse::new)
                .getContent();
        System.out.println(content);
        System.out.println(posts);

        return new PageResponse<>(posts, content);
    }

//    @Cacheable(
//            value = "complaints",
//            key = "#wardNo + '-' + #taluka + '-' + #district + '-' + (#status != null ? #status.name() : 'ALL')"
//    )
//    public List<PostResponse> getComplaintsByLocation(
//            Integer wardNo,
//            String taluka,
//            String district,
//            Status status
//    ) {
//
//        List<Post> posts;
//
//        if (status != null) {
//            posts = repo.findByWardNoAndTalukaAndDistrictAndStatusOrderByCreatedAtDesc(
//                    wardNo, taluka, district, status
//            );
//        } else {
//            posts = repo.findByWardNoAndTalukaAndDistrictOrderByCreatedAtDesc(
//                    wardNo, taluka, district
//            );
//        }
//
//        return posts.stream()
//                .map(PostResponse::new)
//                .toList();
//    }


    public void startWork(Long id) {

        System.out.println(id+"check id:====");
        Post post = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 🔥 SAFETY CHECK (VERY IMPORTANT)
        if (post.getStatus() != Status.ACCEPTED) {
            throw new RuntimeException("Work can only be started if status is ACCEPTED");
        }

        // 🔥 CHANGE STATUS
        post.setStatus(Status.IN_PROGRESS);

        repo.save(post);
    }


    public void completeWork(
            Long postId,
            Double amount,
            MultipartFile image,
            String workerEmail
    ) {

        Post post = repo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getStatus() != Status.IN_PROGRESS) {
            throw new RuntimeException("Work must be IN_PROGRESS");
        }

        // 🔥 upload proof image
        String imageUrl = imageService.upload(image);

        WorkerDTO worker =
                userClient.getWorkerByEmail(workerEmail);

        // 🔥 update complaint
        post.setStatus(Status.COMPLETION_REQUESTED);

        // save proof in post
        post.setWorkImageUrl(imageUrl);
        post.setAmount(amount);


        repo.save(post);

        // 🔥 FIND EXISTING NOTIFICATION
        Notification notification =
                notificationRepo
                        .findByComplaintIdAndWorkerEmail(
                                postId,
                                workerEmail
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found"));

        // 🔥 UPDATE SAME NOTIFICATION
        notification.setMessage(
                "Worker completed work. Verify & approve payment"
        );
        notification.setWardNo(post.getWardNo());
        notification.setStatus(Status.PENDING);

        notification.setProofImageUrl(imageUrl);
        notification.setPaymentDone(false);
        notification.setPaymentStatus(PaymentStatus.PENDING);

        notification.setAmount(amount);

        notification.setUpiId(worker.getUpiId());

        notification.setQrImageUrl(worker.getQrImageUrl());

        notificationRepo.save(notification);

        // 🔥 SEND TO NAGARSEVAK
        notificationService.notifyNagarsevak(notification);
    }

//    public Map<String, Long> getWardComplaintStats(
//            Integer wardNo,
//            String district,
//            String taluka
//    ) {
//        long pending = repo.countByWardNoAndDistrictAndTalukaAndStatus(
//                wardNo, district, taluka, Status.PENDING
//        );
//
//        long inProgress = repo.countByWardNoAndDistrictAndTalukaAndStatus(
//                wardNo, district, taluka, Status.IN_PROGRESS
//        );
//
//        long resolved = repo.countByWardNoAndDistrictAndTalukaAndStatus(
//                wardNo, district, taluka, Status.COMPLETED
//        );
//
//        return Map.of(
//                "pending", pending,
//                "inProgress", inProgress,
//                "resolved", resolved
//        );
//    }

    public Map<String, Long> getWardComplaintStats(
            Integer wardNo,
            String district,
            String taluka
    ) {

        System.out.println("Ward = " + wardNo);
        System.out.println("District = " + district);
        System.out.println("Taluka = " + taluka);

        long pending = repo.countByWardNoAndDistrictAndTalukaAndStatus(
                wardNo, district, taluka, Status.PENDING
        );

        long inProgress = repo.countByWardNoAndDistrictAndTalukaAndStatus(
                wardNo, district, taluka, Status.IN_PROGRESS
        );

        long resolved = repo.countByWardNoAndDistrictAndTalukaAndStatus(
                wardNo, district, taluka, Status.COMPLETED
        );

        System.out.println("Pending = " + pending);
        System.out.println("In Progress = " + inProgress);
        System.out.println("Resolved = " + resolved);

        return Map.of(
                "pending", pending,
                "inProgress", inProgress,
                "resolved", resolved
        );
    }




}
