package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.RoomRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.RoomResponse;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.Room;
import com.KLTN.nguyen.hotelbooking.mapper.RoomMapper;
import com.KLTN.nguyen.hotelbooking.repository.HotelRepository;
import com.KLTN.nguyen.hotelbooking.repository.RoomRepository;
import com.KLTN.nguyen.hotelbooking.repository.TypeRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final TypeRoomRepository typeRoomRepository;
    public RoomResponse addNewRoom(RoomRequest roomRequest){
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find hotel")
        );
        Room room = Room.builder()
                .roomName(roomRequest.getRoomName())
                .price(roomRequest.getPrice())
                .typeRoom(typeRoomRepository.findByCode(roomRequest.getTypeRoomCode()))
                .build();
        roomRepository.save(room);
        return RoomMapper.toResponseDTO(room);
    }
    public RoomResponse updateRoom(Integer roomId, RoomRequest roomRequest){
        Room room = roomRepository.findById(roomId).orElseThrow(()->
                new EntityNotFoundException("Room not found"));
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find hotel")
        );
        room.setRoomName(roomRequest.getRoomName());
        room.setTypeRoom(typeRoomRepository.findByCode(roomRequest.getTypeRoomCode()));
        room.setHotel(hotel);
        room.setPrice(room.getPrice());
        roomRepository.save(room);
        return RoomMapper.toResponseDTO(room);
    }
    public List<RoomResponse> getAllRoomOfHotel(Integer hotelId, Integer pageNumber){
        Pageable page = PageRequest.of(pageNumber, 10);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find hotel"));

        Page<Room> roomPage = roomRepository.findAllByHotel(hotel, page);
        return roomPage.getContent().stream()
                .map(RoomMapper::toResponseDTO)
                .toList();
    }
}
