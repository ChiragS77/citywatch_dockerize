package com.userservice.dto;

import com.userservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfileResponse {

    private String name;
    private String email;
    private Role role;
    private Integer wardNo;
    private String taluka;
    private String district;
    private String profileImage;

}
