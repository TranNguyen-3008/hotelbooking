package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.ReviewRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.ReviewResponse;
import com.KLTN.nguyen.hotelbooking.entity.Booking;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.Review;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.mapper.ReviewMapper;
import com.KLTN.nguyen.hotelbooking.repository.BookingRepository;
import com.KLTN.nguyen.hotelbooking.repository.HotelRepository;
import com.KLTN.nguyen.hotelbooking.repository.ReviewRepository;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    public ReviewResponse addReview(Integer bookingId,Integer id, ReviewRequest request){
        Booking booking = bookingRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("he"));
        User user = userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("User not found")
        );
        Hotel hotel = hotelRepository.findById(booking.getRoom().getHotel().getId()).orElseThrow(
                ()-> new EntityNotFoundException("Hotel not found")
        );
        Review review = Review.builder()
                .user(user)
                .hotel(hotel)
                .comment(request.getComment())
                .star(request.getStar())
                .build();
        reviewRepository.save(review);
        return ReviewMapper.toResponseDTO(review);
    }
    public ReviewResponse changeReview(Integer id, ReviewRequest request){
        Review review = reviewRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Review not found")
        );
        review.setComment(request.getComment());
        review.setStar(request.getStar());
        reviewRepository.save(review);
        return ReviewMapper.toResponseDTO(review);
    }
    public ReviewResponse deleteReview(Integer id){
        Review review = reviewRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Review not found")
        );
        reviewRepository.delete(review);
        return ReviewMapper.toResponseDTO(review);
    }
    public List<ReviewResponse> getReviews(Integer pageNumber){
        Pageable page = PageRequest.of(pageNumber, 10);
        List<Review> reviews = reviewRepository.findAll(page).getContent();
        return reviews.stream().map(ReviewMapper::toResponseDTO).toList();
    }
    public Page<ReviewResponse> getReviewsByHotel(Integer hotelId, Pageable pageable) {
        Page<Review> reviewsPage = reviewRepository.findByHotelId(hotelId, pageable);

        return reviewsPage.map(review -> ReviewResponse.builder()
                .id(review.getId())
                .star(review.getStar())
                .comment(review.getComment())
                .username(review.getUser().getUsername()) // giả sử Review liên kết User
                .hotelName(review.getHotel().getHotelName())    // giả sử Review liên kết Hotel
                .build());
    }
}
