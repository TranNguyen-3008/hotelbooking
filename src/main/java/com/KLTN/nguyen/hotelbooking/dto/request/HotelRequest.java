package com.KLTN.nguyen.hotelbooking.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class HotelRequest {
    private String hotelName;
    private String address;
    private String email;
    private String phoneNumber;
    private Integer provinceId;
}

