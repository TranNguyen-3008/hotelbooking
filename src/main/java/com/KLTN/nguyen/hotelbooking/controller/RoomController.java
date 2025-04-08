package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.RoomResponse;
import com.KLTN.nguyen.hotelbooking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    final RoomService roomService;
    @GetMapping("/{id}")
    public ResponseEntity<List<RoomResponse>> getHotels(@PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(roomService.getAllRoomOfHotel(id ,pageNumber));
    }
}
