package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponse {
    private Integer id;
    private Double price;
    private String image;
    private String typeRoom;
    private String hotelName;
    private String roomName;
}
