package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomId;
    private Integer paymentMethodId;
}
