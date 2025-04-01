package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.dto.response.HotelAttributeResponse;
import com.KLTN.nguyen.hotelbooking.entity.HotelAttributes;

public class HotelAttributeMapper {

    public static HotelAttributeResponse toDTO(HotelAttributes hotelAttribute) {
        return HotelAttributeResponse.builder()
                .id(hotelAttribute.getId())
                .attributeName(hotelAttribute.getAttribute().getAttribute())
                .build();
    }
}