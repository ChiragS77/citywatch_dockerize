package com.userservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.userservice.entity.User;
import com.userservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository repo;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        Map uploadResult  = this.cloudinary.uploader().upload(multipartFile.getBytes(),
                ObjectUtils.asMap("resource_type","auto"));
        return uploadResult.get("secure_url").toString();
    }

    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed");
        }
    }




}
