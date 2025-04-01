package com.KLTN.nguyen.hotelbooking.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelAttributeRequest{
    private Integer hotelId;
    private Integer attributeId;
}

