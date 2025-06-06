package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.EditHotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.service.AuthenticationService;
import com.KLTN.nguyen.hotelbooking.service.CloudinaryService;
import com.KLTN.nguyen.hotelbooking.service.EmailService;
import com.KLTN.nguyen.hotelbooking.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final EmailService emailService;
    @GetMapping("/hotels")
    public Page<HotelResponse> getHotels(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return hotelService.searchHotels(name, location, page, size);
    }
    @GetMapping("/admin/hotels")
    public ResponseEntity<List<HotelResponse>> adminGetHotels(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(hotelService.getHotelss(pageNumber));
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
            hotelService.createHotel(hotelRequest, imageUrl);
            return null;
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
        HotelResponse hotelResponse = hotelService.activateHotel(id);
        emailService.sendSimpleEmail(hotelResponse.getEmail(), "Đã chấp nhận khách sạn", "Khách sạn " + hotelResponse.getHotelName()+ "đã được chấp nhận trên hệ thống" +
                "hãy đăng nhập vào hệ thống bằng tài khoản hệ thống với tên đăng nhập và mật khẩu là email của bạn"
                );

        return ResponseEntity.ok(hotelResponse);
    }
    @PatchMapping("/reject/{id}")
    public ResponseEntity<HotelResponse> rejectHotel(@PathVariable("id") Integer id){
        return ResponseEntity.ok(hotelService.rejectHotel(id));
    }
    @PatchMapping("/hide/{id}")
    public ResponseEntity<HotelResponse> hideHotel(@PathVariable("id") Integer id){
        HotelResponse hotelResponse = hotelService.hideHotel(id);
        emailService.sendSimpleEmail(hotelResponse.getEmail(), "Khách sạn của bạn không đủ tiêu chuẩn để tiếp tục xuất hiện", "Khách sạn " + hotelResponse.getHotelName()+ "đã bị ẩn khỏi hệ thống vì vi phạm tiêu chuẩn. Nếu có sai sót hay liên lạc tại email: ainemrty1@gmail.com"
        );
        return ResponseEntity.ok(hotelResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable("id") Integer id, @RequestBody EditHotelRequest hotelRequest){
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
