package com.userservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreatePoliticalUserRequest {

    private String name;
    private String email;
    private String password;

    private String state;
    private String district;
    private String taluka;
    private Integer wardNo;

    private String partyName;
    private MultipartFile partyImage;
}
