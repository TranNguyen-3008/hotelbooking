package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelAttributesUpdateRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelAttributeResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelPhotoResponse;
import com.KLTN.nguyen.hotelbooking.entity.Attribute;
import com.KLTN.nguyen.hotelbooking.entity.HotelPhoto;
import com.KLTN.nguyen.hotelbooking.service.CloudinaryService;
import com.KLTN.nguyen.hotelbooking.service.HotelAttributeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attribute")
@RequiredArgsConstructor
public class HotelAttributeController {
    final HotelAttributeService hotelAttributeService;
    final CloudinaryService cloudinaryService;
    @GetMapping("/{id}")
    public ResponseEntity<List<HotelAttributeResponse>> getAttributes(@PathVariable("id")
                                                      Integer hotelId) {
        return ResponseEntity.ok(hotelAttributeService.getAttributesByHotelId(hotelId));

    }
    @GetMapping()
    public ResponseEntity<List<Attribute>> getAllAttributes() {
        return ResponseEntity.ok(hotelAttributeService.getAllAttributes());

    }
    @PutMapping("/{hotelId}")
    public ResponseEntity<?> updateHotelAttributes(
            @PathVariable Integer hotelId,
            @RequestBody List<String> attributes
    ) {
        try {
            hotelAttributeService.updateHotelAttributes(hotelId, attributes);
            return ResponseEntity.ok().body("Cập nhật thuộc tính thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật: " + e.getMessage());
        }
    }
    @PostMapping("/{hotelId}")
    public ResponseEntity<?> addHotelPhoto(@PathVariable("hotelId") Integer hotelId,
                                           @RequestPart("photo") MultipartFile photo){
        try {
            String imageUrl = cloudinaryService.uploadToCloudinary(photo,"image", "Image");
            HotelPhotoResponse hotelPhotoResponse =  hotelAttributeService.addPhoto(hotelId, imageUrl);
            return ResponseEntity.ok(hotelPhotoResponse);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload hotel photo: " + e.getMessage());
        }
    }
}
