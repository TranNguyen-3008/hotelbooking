package com.KLTN.nguyen.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private int star;
    private String comment;
    private String username;
    private String hotelName;
}
