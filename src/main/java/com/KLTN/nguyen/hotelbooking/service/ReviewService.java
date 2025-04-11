package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.ReviewRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.ReviewResponse;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.Review;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.mapper.ReviewMapper;
import com.KLTN.nguyen.hotelbooking.repository.HotelRepository;
import com.KLTN.nguyen.hotelbooking.repository.ReviewRepository;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    public ReviewResponse addReview(Integer id, ReviewRequest request){
        User user = userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("User not found")
        );
        Hotel hotel = hotelRepository.findById(request.getHotelId()).orElseThrow(
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

}
