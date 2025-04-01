package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelPhotoRequest {
    private String urlPhoto;
    private Integer hotelId;
}