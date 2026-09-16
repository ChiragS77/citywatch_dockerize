package com.userservice.dto;

import com.userservice.entity.Role;
import com.userservice.entity.WorkType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterReq {

    private String name;
    private String email;
    private String password;

    private String state;
    private String district;
    private String taluka;
    private Integer wardNo;
    private LocalDate dob;
    private WorkType workType;

    @Enumerated(EnumType.STRING)
    private Role  role;


}
