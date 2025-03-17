package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.entity.*;
import com.KLTN.nguyen.hotelbooking.request.BookingRequest;
import com.KLTN.nguyen.hotelbooking.response.BookingResponse;

public class BookingMapper {
    public static BookingResponse toResponseDTO(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().getStatus())
                .roomType(booking.getRoom().getTypeRoom().getDescription())
                .hotelName(booking.getRoom().getHotel().getHotelName())
                .paymentMethod(booking.getPaymentMethod().getMethodName())
                .build();
    }
}
