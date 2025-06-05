package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.RoomRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelAttributeResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.RoomResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.TypeRoomResponse;
import com.KLTN.nguyen.hotelbooking.entity.TypeRoom;
import com.KLTN.nguyen.hotelbooking.repository.TypeRoomRepository;
import com.KLTN.nguyen.hotelbooking.service.CloudinaryService;
import com.KLTN.nguyen.hotelbooking.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    final CloudinaryService cloudinaryService;
    final RoomService roomService;
    final TypeRoomRepository typeRoomRepository;
    @GetMapping("/{hotelId}")
    public ResponseEntity<List<RoomResponse>> getHotels(@PathVariable("hotelId") Integer id, @RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(roomService.getAllRoomOfHotel(id ,pageNumber));
    }
    @PostMapping(value = "/room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createHotel(
            @RequestPart("image") MultipartFile file,
            @RequestPart("roomRequest") String roomRequestJson
    ) {
        try {
            // Parse JSON sang object
            ObjectMapper objectMapper = new ObjectMapper();
            RoomRequest roomRequest = objectMapper.readValue(roomRequestJson, RoomRequest.class);

            String imageUrl = cloudinaryService.uploadToCloudinary(file,"image", "Image");
            roomRequest.setImage(imageUrl);

            return ResponseEntity.ok(roomService.addNewRoom(roomRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<TypeRoom>> getAllAttributes(){
        return ResponseEntity.ok(typeRoomRepository.findAll());
    }
}
