package com.complaintservice.controller;

import com.complaintservice.dto.*;
import com.complaintservice.entity.Post;
import com.complaintservice.entity.PostType;
import com.complaintservice.entity.Status;
import com.complaintservice.entity.WorkType;
import com.complaintservice.repository.PostRepository;
import com.complaintservice.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;
    private final ImageService imageService;
    private final LikeService likeService;
    private final CommentSerivce commentSerivce;
    private final PostRepository repo;
    private final AssignmentService assignmentService;

    @PostMapping("/upload")
    public Post createPostWithImage(
            @RequestParam("caption") String caption,
            @RequestParam("type") String type,
            @RequestParam(value = "workType", required = false) String workType, // 🔥 NEW
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String district = (String) request.getAttribute("district");
        String taluka = (String) request.getAttribute("taluka");

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = imageService.upload(image);
        }

        Post post = new Post();
        post.setCaption(caption);
        post.setType(PostType.valueOf(type));

        if (workType != null) {
            post.setWorkType(WorkType.valueOf(workType));
        }
        post.setImageUrl(imageUrl);


        return service.createPost(post, username, wardNo, district, taluka);
    }




    // ✅ LIKE
    @PostMapping("/{postId}/like")
    public Map<String, Object> toggleLike(
            @PathVariable Long postId,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");

        return likeService.toggleLike(postId, username);
    }

    // ✅ LIKE COUNT
    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<Long> likeCount(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }

    // ✅ ADD COMMENT
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest body,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");

        CommentResponse res = commentSerivce.addComment(
                postId,
                body.getText(),
                username
        );

        return ResponseEntity.ok(res);
    }

    // ✅ GET COMMENTS
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long postId) {

        return ResponseEntity.ok(commentSerivce.getComments(postId));
    }



    @GetMapping("/getFeedPage")
    public List<PostResponse> getFeedPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");

        return service.getFeed(wardNo, username, role, taluka, district, page, size);
    }

    @GetMapping("/getFeed")
    public List<PostResponse> getFeed(HttpServletRequest request) {
        System.out.print("Enter into get feed method...");

        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");

        return service.getFeed(wardNo, username, role, taluka, district);
    }

    @GetMapping("/my-posts")
    public Page<PostResponse> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        System.out.println("================"+username+"=======================");

        return service.getUserPosts(username, page, size);
    }







    @GetMapping("/complaints/status")
    public List<Post> getByStatus(
            @RequestParam Status status,
            HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");

        return repo.findByWardNoAndStatusOrderByCreatedAtDesc(wardNo, status);
    }

//
//    @PutMapping("/{id}/assign")
//    public Post assignWorker(@PathVariable Long id,
//                             @RequestParam String worker,
//                             HttpServletRequest request) {
//
//        Post post = repo.findById(id).orElseThrow();
//
//
//        return repo.save(post);
//    }


//    @PutMapping("/{id}/assign")
//    public String assignWorker(@PathVariable Long id,
//                               @RequestParam String workerEmail) {
//        System.out.println("Worker email: " + workerEmail);
//
//        assignmentService.assignWorker(id, workerEmail);
//
//        return "Worker assigned successfully";
//    }
@PutMapping("/{id}/assign")
public ResponseEntity<?> assignWorker(@PathVariable Long id,
                                      @RequestParam String workerEmail) {

    System.out.println("Worker email: " + workerEmail);

    assignmentService.assignWorker(id, workerEmail);

    return ResponseEntity.ok("Worker assigned successfully");
}



    @GetMapping("/recent")
    public List<Post> recent(HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");

        return repo.findTop10ByWardNoOrderByCreatedAtDesc(wardNo);
    }

    @GetMapping("/{id}")
    public Post getComplaint(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }


    @GetMapping("/complaints/count")
    public Long getComplaintCount(
            @RequestParam(required = false) Status status,
            HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");
        System.out.println(wardNo+" Ward no");
        System.out.println(taluka+" Taluka");
        System.out.println(district+" District");

        return repo.countComplaintsWithLocationFilter(
                wardNo,
                taluka,
                district,
                status
        );
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats(HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");

        long total = repo.countComplaintsWithLocationFilter(
                wardNo, taluka, district, null
        );
        System.out.println(total);

        long pending = repo.countComplaintsWithLocationFilter(
                wardNo, taluka, district, Status.PENDING
        );
        System.out.println(pending);

        long resolved = repo.countComplaintsWithLocationFilter(
                wardNo, taluka, district, Status.COMPLETED
        );
        System.out.println(resolved);
        return Map.of(
                "total", total,
                "pending", pending,
                "resolved", resolved
        );
    }


    @GetMapping("/complaints")
    public PageResponse<PostResponse> getComplaints( @RequestParam(required = false) Status status,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size,
                                                                                HttpServletRequest request ) {
        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");
        return service.getComplaintsByLocation( wardNo, taluka, district, status, page, size ); }


    @PutMapping("/{id}/status")
    public Post updateStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            HttpServletRequest request) {

        String role = (String) request.getAttribute("role");
        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String taluka = (String) request.getAttribute("taluka");
        String district = (String) request.getAttribute("district");

        return service.updateStatus(id, status, role, wardNo, taluka, district);
    }


    @PutMapping("/{id}/start")
    public String startWork(@PathVariable Long id) {

        System.out.println("==================== Enter into method =================="+id);
        service.startWork(id);

        return "Work started successfully";
    }

    @PutMapping("/{id}/complete")
    public void completeWork(
            @PathVariable Long id,
            @RequestParam Double amount,
            @RequestParam MultipartFile image,
            HttpServletRequest request
    ) {
        String workerEmail =
                (String) request.getAttribute("username");

        service.completeWork(id, amount, image, workerEmail);
    }


    @GetMapping("/ward-stats")
    public Map<String, Long> getWardStats(HttpServletRequest request) {

        Integer wardNo = (Integer) request.getAttribute("wardNo");
        String district = (String) request.getAttribute("district");
        String taluka = (String) request.getAttribute("taluka");

        System.out.println("========== WARD STATS ==========");
        System.out.println("Ward No   : " + wardNo);
        System.out.println("District  : " + district);
        System.out.println("Taluka    : " + taluka);
        System.out.println("================================");

        return service.getWardComplaintStats(wardNo, district, taluka);
    }
}
