package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.service.CloudinaryService;
import com.KLTN.nguyen.hotelbooking.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final CloudinaryService cloudinaryService;
    private final HotelService hotelService;
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelResponse>> getHotels(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(hotelService.getHotels(pageNumber));
    }
    @GetMapping("/hotels/status")
    public ResponseEntity<List<HotelResponse>> getHotelsByStatus(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,@RequestParam("status") String status){
        return ResponseEntity.ok(hotelService.getHotelByStatus(status,pageNumber));
    }
    @PostMapping(value = "/hotel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createHotel(
            @RequestPart("image") MultipartFile file,
            @RequestPart("hotelRequest") String hotelRequestJson // <-- Dùng String thay vì HotelRequest
    ) {
        try {
            // Parse JSON sang object
            ObjectMapper objectMapper = new ObjectMapper();
            HotelRequest hotelRequest = objectMapper.readValue(hotelRequestJson, HotelRequest.class);

            // Upload ảnh
            String imageUrl = cloudinaryService.uploadToCloudinary(file,"image", "Image");
            hotelRequest.setImage(imageUrl);

            return ResponseEntity.ok(hotelService.createHotel(hotelRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<HotelResponse> activateHotel(@PathVariable("id") Integer id){
        return ResponseEntity.ok(hotelService.activateHotel(id));
    }
    @PatchMapping("/reject/{id}")
    public ResponseEntity<HotelResponse> rejectHotel(@PathVariable("id") Integer id){
        return ResponseEntity.ok(hotelService.rejectHotel(id));
    }
    @PatchMapping("/hide/{id}")
    public ResponseEntity<HotelResponse> hideHotel(@PathVariable("id") Integer id){
        return ResponseEntity.ok(hotelService.hideHotel(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable("id") Integer id, @RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.updateHotel(id, hotelRequest));
    }

}
