package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.BookingRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.BookingResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelRevenue;
import com.KLTN.nguyen.hotelbooking.entity.BookingStatus;
import com.KLTN.nguyen.hotelbooking.repository.BookingStatusRepository;
import com.KLTN.nguyen.hotelbooking.service.AuthenticationService;
import com.KLTN.nguyen.hotelbooking.service.BookingService;
import com.KLTN.nguyen.hotelbooking.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final EmailService emailService;
    private final AuthenticationService authenticationService;
    private final BookingStatusRepository bookingStatusRepository;
    @GetMapping("/me")
    public ResponseEntity<List<BookingResponse>> getBookingByUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber) {
        try {
            if (pageNumber < 0) {
                return ResponseEntity.badRequest().build();
            }
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authorizationHeader.substring(7);
            Long userId = authenticationService.getUserIdFromToken(token);
            Integer id = userId.intValue();

            List<BookingResponse> bookingResponses = bookingService.getBookingByUser(id, pageNumber);
            return ResponseEntity.ok(bookingResponses);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest bookingRequest) {
            BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);
            emailService.sendSimpleEmail(bookingRequest.getEmail(), "Xác nhận đặt phòng", "Đơn đặt phòng của bạn đã được tạo với id là " + bookingResponse.getId());
            return ResponseEntity.ok(bookingResponse);
    }
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<Page<BookingResponse>> getBookingsByHotelId(
            @PathVariable Integer hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingResponse> bookings = bookingService.getBookingsByHotelAndStatus(hotelId, status, pageable);

        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getAllBookingStatuses(){
        List<BookingStatus> lst = bookingStatusRepository.findAll();
        List<String> lst1 = lst.stream().map(BookingStatus::getStatus).toList();
        return ResponseEntity.ok(lst1);
    }
    @GetMapping("/hotel/{hotelId}/calendar")
    public ResponseEntity<List<BookingResponse>> getBookingsForCalendar(
            @PathVariable Integer hotelId,
            @RequestParam String startDate,   // định dạng yyyy-MM-dd
            @RequestParam String endDate) {

        List<BookingResponse> bookings = bookingService.getBookingsForCalendar(hotelId, startDate, endDate);
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/by-end-date/{hotelId}")
    public ResponseEntity<List<HotelRevenue>> getRevenueByEndDateWithFilter(
            @PathVariable Integer hotelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(bookingService.getRevenueByEndDate(hotelId, startDate, endDate));
    }
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Integer bookingId,
            @RequestParam String status) {
            if(status.equals("CANCELLED")){
                status = "ĐÃ HỦY";
            }
            BookingResponse bookingResponse = bookingService.updateBookingStatus(bookingId, status);
            return ResponseEntity.ok(bookingResponse);
    }
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Integer bookingId) {
        BookingResponse bookingResponse = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(bookingResponse);
    }
}

