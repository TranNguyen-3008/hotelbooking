package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.Room;
import com.KLTN.nguyen.hotelbooking.entity.TypeRoom;
import com.KLTN.nguyen.hotelbooking.dto.request.RoomRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.RoomResponse;

public class RoomMapper {

    public static Room toEntity(RoomRequest dto, Hotel hotel, TypeRoom typeRoom) {
        return Room.builder()
                .price(dto.getPrice())
                .hotel(hotel)
                .typeRoom(typeRoom)
                .build();
    }

    public static RoomResponse toResponseDTO(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .price(room.getPrice())
                .hotelName(room.getHotel().getHotelName())
                .typeRoom(room.getTypeRoom().getDescription())
                .build();
    }
}
