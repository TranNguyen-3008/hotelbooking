package com.KLTN.nguyen.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private Double price;
    private String typeRoom;
    private String hotelName;
}
