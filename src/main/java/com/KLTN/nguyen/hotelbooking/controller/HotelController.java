package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.service.AuthenticationService;
import com.KLTN.nguyen.hotelbooking.service.CloudinaryService;
import com.KLTN.nguyen.hotelbooking.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final CloudinaryService cloudinaryService;
    private final HotelService hotelService;
    private final AuthenticationService authenticationService;
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
            @RequestPart("hotelRequest") String hotelRequestJson
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
    @GetMapping("/search")
    public ResponseEntity<List<HotelResponse>> searchHotels(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(hotelService.searchHotels(keyword, page));
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
    @GetMapping("/owner/hotel")
    public ResponseEntity<HotelResponse> getHotelForOwner(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Loại bỏ 'Bearer ' từ token
            Long userId = authenticationService.getUserIdFromToken(token);
            Integer id = userId.intValue();
            HotelResponse hotel = hotelService.getHotelByHotelOwner(id);
            return ResponseEntity.ok(hotel);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Trả về BadRequest nếu có lỗi
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }


}
