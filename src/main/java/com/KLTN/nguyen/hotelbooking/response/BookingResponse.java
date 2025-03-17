package com.KLTN.nguyen.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate bookingDate;
    private Double totalPrice;
    private String status;
    private String roomType;
    private String hotelName;
    private String paymentMethod;
}
