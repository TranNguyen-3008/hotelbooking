package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.entity.*;
import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;

public class HotelMapper {

    public static Hotel toEntity(HotelRequest dto, Province province, User owner, HotelStatus status) {
        return Hotel.builder()
                .hotelName(dto.getHotelName())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .image(dto.getImage())
                .phoneNumber(dto.getPhoneNumber())
                .province(province)
                .owner(owner)
                .status(status)
                .build();
    }

    public static HotelResponse toResponseDTO(Hotel hotel) {
        return HotelResponse.builder()
                .id(hotel.getId())
                .hotelName(hotel.getHotelName())
                .address(hotel.getAddress())
                .email(hotel.getEmail())
                .image(hotel.getImage())
                .phoneNumber(hotel.getPhoneNumber())
                .provinceName(hotel.getProvince() != null ? hotel.getProvince().getProvinceName() : null)
                .ownerId(hotel.getOwner() != null ? hotel.getOwner().getId() : null)
                .ownerEmail(hotel.getOwner() != null ? hotel.getOwner().getEmail() : null)
                .statusDescription(hotel.getStatus().getDescription())
                .attributes(
                        hotel.getAttributes().stream()
                                .map(hotelAttr -> hotelAttr.getAttribute().getAttribute()) // Lấy tên thuộc tính
                                .toList()
                )
                .photoUrls(hotel.getPhotos().stream().map(photo->photo.getUrlPhoto()).toList())
                .build();
    }
}
