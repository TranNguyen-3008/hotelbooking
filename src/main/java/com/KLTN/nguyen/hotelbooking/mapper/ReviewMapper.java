package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.entity.Review;
import com.KLTN.nguyen.hotelbooking.dto.response.ReviewResponse;

public class ReviewMapper {
    public static ReviewResponse toResponseDTO(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .star(review.getStar())
                .comment(review.getComment())
                .username(review.getUser().getFullName())
                .hotelName(review.getHotel().getHotelName())
                .build();
    }
}
