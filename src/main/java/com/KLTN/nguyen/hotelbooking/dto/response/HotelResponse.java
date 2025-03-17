package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HotelResponse {
    private Integer id;
    private String hotelName;
    private String image;
    private String address;
    private String email;
    private String phoneNumber;
    private String provinceName;
    private String ownerEmail;
    private Integer ownerId;
    private String statusDescription;
    private List<String> attributes;
    private List<String> photoUrls;
}

