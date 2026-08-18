package com.complaintservice.dto;

import com.complaintservice.entity.Post;
import com.complaintservice.entity.PostType;
import com.complaintservice.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;
    private String caption;
    private String imageUrl;
    private String type;
    private String priority;
    private String status;
    private Integer wardNo;
    private String username;
    private String district;
    private String taluka;
    private LocalDateTime createdAt;
    private String assignedTo;
    private String workType;

    // ✅ NEW FIELDS
    private Long likeCount;
    private Long commentCount;
    private Boolean isLiked;

    // ✅ CONSTRUCTOR FROM ENTITY
    public PostResponse(Post post) {
        this.id = post.getId();
        this.caption = post.getCaption();
        this.imageUrl = post.getImageUrl();
        this.type = post.getType() != null ? post.getType().name() : null;
        this.priority = post.getPriority() != null ? post.getPriority().name() : null;
        this.status = post.getStatus() != null ? post.getStatus().name() : null;
        this.wardNo = post.getWardNo();
        this.username = post.getUsername();
        this.district = post.getDistrict();
        this.taluka = post.getTaluka();
        this.createdAt = post.getCreatedAt();
        this.workType = post.getWorkType() != null
                ? post.getWorkType().name()
                : null;
    }


}
