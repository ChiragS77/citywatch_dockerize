package com.complaintservice.client;

import com.complaintservice.dto.WorkerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(name = "UserSer")
public interface UserClient {

    @GetMapping("/user/nagarsevak/{wardNo}")
    List<String> getNagarsevakEmails(@PathVariable Integer wardNo);

    @GetMapping("/worker/email")
    WorkerDTO getWorkerByEmail(
            @RequestParam String email);
}
