package com.KLTN.nguyen.hotelbooking.response;

import lombok.Data;

import java.util.List;

@Data
public class HotelResponse {
    private Long id;
    private String hotelName;
    private String image;
    private String address;
    private String email;
    private String phoneNumber;
    private String provinceName;
    private String status;
    private List<String> attributes;
    private List<String> photoUrls;
}

