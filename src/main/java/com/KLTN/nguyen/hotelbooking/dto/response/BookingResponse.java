package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingResponse {
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate bookingDate;
    private Double totalPrice;
    private String status;
    private String roomName;
    private String hotelName;
    private String paymentMethod;
    private String email;
}
