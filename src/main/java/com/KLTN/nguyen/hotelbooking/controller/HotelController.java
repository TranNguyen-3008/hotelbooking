package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.service.CloudinaryService;
import com.KLTN.nguyen.hotelbooking.service.HotelService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final CloudinaryService cloudinaryService;
    private final HotelService hotelService;
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelResponse>> getUsers(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(hotelService.getHotels(pageNumber));
    }
    @PostMapping("/hotel")
    public ResponseEntity<Object> createHotel(@RequestPart("image") MultipartFile file, @RequestPart("hotelRequest") HotelRequest hotelRequest) {
        try {
            String imageUrl = cloudinaryService.uploadToCloudinary(file,"image", "Image");
            hotelRequest.setImage(imageUrl);
            return ResponseEntity.ok(hotelService.createHotel(hotelRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        }


}
