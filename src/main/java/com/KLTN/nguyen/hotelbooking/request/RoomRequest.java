package com.KLTN.nguyen.hotelbooking.request;

import lombok.Data;

@Data
public class RoomRequest {
    private Double price;
    private String typeRoomCode;
    private Long hotelId;
}

