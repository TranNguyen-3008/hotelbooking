package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.entity.*;
import com.KLTN.nguyen.hotelbooking.dto.response.BookingResponse;

public class BookingMapper {
    public static BookingResponse toResponseDTO(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().getStatus())
                .roomName(booking.getRoom().getRoomName())
                .email(booking.getUser().getEmail())
                .hotelName(booking.getRoom().getHotel().getHotelName())
                .paymentMethod(booking.getPaymentMethod() != null
                        ? booking.getPaymentMethod().getMethodName()
                        : "Chưa có phương thức thanh toán")
                .build();
    }
}
