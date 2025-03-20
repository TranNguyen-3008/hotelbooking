package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class HotelRequest {
    private String hotelName;
    private String image;
    private String address;
    private String email;
    private String phoneNumber;
    private Integer provinceId;
    private Integer ownerId;
    private String statusCode;
    private List<Integer> attributeIds;
}

