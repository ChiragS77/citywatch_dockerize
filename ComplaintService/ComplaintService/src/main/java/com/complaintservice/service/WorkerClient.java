package com.complaintservice.service;

import com.complaintservice.dto.WorkerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface WorkerClient {

    @GetMapping("/workers")
    List<WorkerDTO> getWorkers(
            @RequestParam("type") String type,
            @RequestHeader("Cookie") String cookie
    );
}
