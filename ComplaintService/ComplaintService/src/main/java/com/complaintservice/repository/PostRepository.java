package com.complaintservice.repository;

import com.complaintservice.entity.Post;
import com.complaintservice.entity.PostType;
import com.complaintservice.entity.Status;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByWardNo(Integer wardNo, Pageable pageable);


    Page<Post> findByDistrictAndTaluka(String district, String taluka, Pageable pageable);

    Page<Post> findByDistrictAndTalukaAndWardNo(String district, String taluka, Integer wardNo, Pageable pageable);

    Page<Post> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable);


    List<Post> findByWardNoAndType(Integer wardNo, PostType type);


    // Total complaints
    Long countByWardNoAndType(Integer wardNo, PostType type);

    // Count by status
    Long countByWardNoAndStatus(Integer wardNo, Status status);

    List<Post> findByWardNoAndTypeOrderByCreatedAtDesc(
            Integer wardNo,
            PostType type
    );

    List<Post> findByWardNoAndStatusOrderByCreatedAtDesc(
            Integer wardNo,
            Status status
    );

    List<Post> findTop10ByWardNoOrderByCreatedAtDesc(Integer wardNo);

//    List<Post> findByAssignedToAndWardNo(String assignedTo, Integer wardNo);

    @Query("""
SELECT p FROM Post p 
WHERE p.wardNo = :wardNo 
AND (:status IS NULL OR p.status = :status)
AND p.type = 'COMPLAINT'
ORDER BY p.createdAt DESC
""")
    List<Post> findComplaintsWithFilter(
            @Param("wardNo") Integer wardNo,
            @Param("status") Status status
    );


    @Query("""
SELECT COUNT(p) FROM Post p 
WHERE p.wardNo = :wardNo
AND p.taluka = :taluka
AND p.district = :district
AND p.type = 'POST'
AND (:status IS NULL OR p.status = :status)
""")
    Long countComplaintsWithLocationFilter(
            @Param("wardNo") Integer wardNo,
            @Param("taluka") String taluka,
            @Param("district") String district,
            @Param("status") Status status
    );

    @Query("""
SELECT 
    COUNT(p),
    SUM(CASE WHEN p.status = 'PENDING' THEN 1 ELSE 0 END),
    SUM(CASE WHEN p.status = 'COMPLETED' THEN 1 ELSE 0 END)
FROM Post p
WHERE p.wardNo = :wardNo
AND p.taluka = :taluka
AND p.district = :district
AND p.type = 'COMPLAINT'
""")
    Object[] getComplaintStats(
            @Param("wardNo") Integer wardNo,
            @Param("taluka") String taluka,
            @Param("district") String district
    );

    List<Post> findByWardNoAndTalukaAndDistrictAndTypeOrderByCreatedAtDesc(
            Integer wardNo,
            String taluka,
            String district,
            PostType type
    );

    Page<Post> findByWardNoAndTalukaAndDistrictAndTypeOrderByCreatedAtDesc(
            Integer wardNo,
            String taluka,
            String district,
            PostType type,
            Pageable pageable
    );

//    Page<Post> findByWardNoAndTalukaAndDistrictOrderByCreatedAtDesc(
//            Integer wardNo,
//            String taluka,
//            String district,
//            Pageable pageable
//    );
//
//    Page<Post> findByWardNoAndTalukaAndDistrictAndStatusOrderByCreatedAtDesc(
//            Integer wardNo,
//            String taluka,
//            String district,
//            Status status,
//            Pageable pageable
//    );

    Page<Post> findByWardNoAndTalukaAndDistrictOrderByCreatedAtDesc( Integer wardNo,
                                                                     String taluka,
                                                                     String district,
                                                                     Pageable pageable );

    Page<Post> findByWardNoAndTalukaAndDistrictAndStatusOrderByCreatedAtDesc( Integer wardNo,
                                                                              String taluka,
                                                                              String district,
                                                                              Status status,
                                                                              Pageable pageable );


    List<Post> findByDistrictAndTalukaOrderByCreatedAtDesc(
            String district,
            String taluka
    );

    List<Post> findByDistrictAndTalukaAndWardNoOrderByCreatedAtDesc(
            String district,
            String taluka,
            Integer wardNo
    );

    Long countByWardNoAndDistrictAndTalukaAndStatus(
            Integer wardNo,
            String district,
            String taluka,
            Status status
    );
}
