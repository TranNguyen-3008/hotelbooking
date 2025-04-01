package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.response.HotelAttributeResponse;
import com.KLTN.nguyen.hotelbooking.service.HotelAttributeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attribute")
@RequiredArgsConstructor
public class HotelAttributeController {
    final HotelAttributeService hotelAttributeService;
    @GetMapping("/{id}")
    public ResponseEntity<List<HotelAttributeResponse>> getAttributes(@PathVariable("id")
                                                      Integer hotelId){
        return ResponseEntity.ok(hotelAttributeService.getAttributesByHotelId(hotelId));

    }
}
