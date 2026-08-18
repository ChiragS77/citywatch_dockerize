package com.userservice.service;

import com.userservice.dto.ServiceHealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AdminHealthService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ServiceHealthResponse> checkAllServices() {

        return List.of(
                check("API Gateway", "http://localhost:8080/actuator/health"),
                check("User Service", "http://localhost:8081/actuator/health"),
                check("Complaint Service", "http://localhost:8082/actuator/health"),
                check("Eureka Server", "http://localhost:8761/actuator/health")
        );
    }

    private ServiceHealthResponse check(String name, String url) {
        long start = System.currentTimeMillis();

        try {
            Map res = restTemplate.getForObject(url, Map.class);

            long end = System.currentTimeMillis();
            String responseTime = (end - start) + " ms";

            String status = res.get("status").toString();

            String uptime = status.equals("UP") ? "99.99%" : "0%";

            return new ServiceHealthResponse(
                    name,
                    url,
                    status,
                    responseTime,
                    uptime
            );

        } catch (Exception e) {

            long end = System.currentTimeMillis();
            String responseTime = (end - start) + " ms";

            return new ServiceHealthResponse(
                    name,
                    url,
                    "DOWN",
                    responseTime,
                    "0%"
            );
        }
    }


}
