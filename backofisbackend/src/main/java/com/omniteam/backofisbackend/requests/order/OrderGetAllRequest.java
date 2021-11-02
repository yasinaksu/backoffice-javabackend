package com.omniteam.backofisbackend.requests.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderGetAllRequest {
    private int customerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private int page=0;
    private int size=20;
}
