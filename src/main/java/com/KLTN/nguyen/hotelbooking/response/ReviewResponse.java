package com.KLTN.nguyen.hotelbooking.response;

import lombok.Data;

@Data
public class ReviewResponse {
    private Long id;
    private int star;
    private String comment;
    private String username;
    private String hotelName;
}
