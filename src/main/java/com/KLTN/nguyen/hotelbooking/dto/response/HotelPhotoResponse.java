package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelPhotoResponse {
    private Integer id;
    private String urlPhoto;
    private Integer hotelId;
}