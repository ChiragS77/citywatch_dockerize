package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminAlertResponse {

    private String type;      // danger, warning, info
    private String title;
    private String message;
    private String time;
}
