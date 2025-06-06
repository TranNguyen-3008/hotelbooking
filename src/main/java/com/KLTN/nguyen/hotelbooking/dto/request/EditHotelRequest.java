package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

@Data
public class EditHotelRequest {
    private String hotelName;
    private String address;
    private String email;
    private String phoneNumber;
}
