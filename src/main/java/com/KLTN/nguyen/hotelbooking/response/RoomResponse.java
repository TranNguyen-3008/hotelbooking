package com.KLTN.nguyen.hotelbooking.response;

import lombok.Data;

@Data
public class RoomResponse {
    private Long id;
    private Double price;
    private String typeRoom;
    private String hotelName;
}
