package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceHealthResponse {

    private String serviceName;
    private String url;
    private String status;
    private String responseTime;
    private String uptime;

}
