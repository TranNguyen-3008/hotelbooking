package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomId;
    private String methodName;
    private String email;
    private Double price;
}
