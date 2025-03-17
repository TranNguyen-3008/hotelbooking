package com.KLTN.nguyen.hotelbooking.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long roomId;
    private Long paymentMethodId;
}
