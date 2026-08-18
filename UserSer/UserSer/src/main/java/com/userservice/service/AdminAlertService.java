package com.userservice.service;

import com.userservice.dto.AdminAlertResponse;
import com.userservice.repo.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAlertService {


    private final AdminHealthService adminHealthService;
    private final WorkerRepository workerRepository;

    public List<AdminAlertResponse> getAlerts() {

        List<AdminAlertResponse> alerts = new ArrayList<>();

        long downServices = adminHealthService.checkAllServices()
                .stream()
                .filter(s -> s.getStatus().equalsIgnoreCase("DOWN"))
                .count();

        if (downServices > 0) {
            alerts.add(new AdminAlertResponse(
                    "danger",
                    downServices + " Services are Down",
                    "Some services are not responding",
                    LocalTime.now().toString().substring(0, 5)
            ));
        }

        long inactiveWorkers = workerRepository.findAll()
                .stream()
                .filter(w -> Boolean.FALSE.equals(w.getAvailable()))
                .count();

        if (inactiveWorkers > 0) {
            alerts.add(new AdminAlertResponse(
                    "warning",
                    inactiveWorkers + " Workers are Inactive",
                    "Workers are currently unavailable",
                    LocalTime.now().toString().substring(0, 5)
            ));
        }

        alerts.add(new AdminAlertResponse(
                "info",
                "System Health Checked",
                "Latest system scan completed",
                LocalDate.now().toString()
        ));

        return alerts;
    }
}
