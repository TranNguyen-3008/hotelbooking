package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

@Data
public class RoomRequest {
    private Double price;
    private String typeRoomCode;
    private Integer hotelId;
}

