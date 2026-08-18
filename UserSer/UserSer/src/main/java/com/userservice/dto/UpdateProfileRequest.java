package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    private String name;
    private String state;
    private String district;
    private String taluka;
    private Integer wardNo;
}
