package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.ReviewRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.ReviewResponse;
import com.KLTN.nguyen.hotelbooking.entity.Review;
import com.KLTN.nguyen.hotelbooking.repository.ReviewRepository;
import com.KLTN.nguyen.hotelbooking.service.AuthenticationService;
import com.KLTN.nguyen.hotelbooking.service.BookingService;
import com.KLTN.nguyen.hotelbooking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final AuthenticationService authenticationService;
    private final BookingService bookingService;
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(reviewService.getReviews(pageNumber));
    }
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByHotel(
            @PathVariable Integer hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<ReviewResponse> reviews = reviewService.getReviewsByHotel(hotelId, PageRequest.of(page, size));
        return ResponseEntity.ok(reviews);
    }
    @PostMapping("/{id}")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable("id") Integer id,
                                                       @RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody ReviewRequest request){
        try {
            String token = authorizationHeader.substring(7);
            Long userId = authenticationService.getUserIdFromToken(token);
            Integer userId1 = userId.intValue();
            bookingService.reviewedBooking(id);
            return ResponseEntity.ok(reviewService.addReview(id, userId1, request));
        }catch (Exception e){
            return ResponseEntity.status(400).body(null);
        }
    }

}
