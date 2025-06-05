package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.Room;
import com.KLTN.nguyen.hotelbooking.entity.TypeRoom;
import com.KLTN.nguyen.hotelbooking.dto.request.RoomRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.RoomResponse;

public class RoomMapper {

    public static RoomResponse toResponseDTO(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .price(room.getPrice())
                .hotelName(room.getHotel().getHotelName())
                .roomName(room.getRoomName())
                .typeRoom(room.getTypeRoom().getDescription())
                .image(room.getImage())
                .build();
    }
}
