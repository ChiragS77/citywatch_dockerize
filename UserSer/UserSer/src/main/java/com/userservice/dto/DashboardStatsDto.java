package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DashboardStatsDto {

    private Long totalNagarsevaks;
    private Long totalNagaradhyaksha;
    private Long totalWorkers;
    private Long totalCitizens;

}
