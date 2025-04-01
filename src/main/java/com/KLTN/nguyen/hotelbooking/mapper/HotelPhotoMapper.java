package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.dto.response.HotelPhotoResponse;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.HotelPhoto;

public class HotelPhotoMapper {

    public static HotelPhotoResponse toResponseDTO(HotelPhoto hotelPhoto) {
        return HotelPhotoResponse.builder()
                .id(hotelPhoto.getId())
                .urlPhoto(hotelPhoto.getUrlPhoto())
                .hotelId(hotelPhoto.getHotel().getId())
                .build();
    }
}
