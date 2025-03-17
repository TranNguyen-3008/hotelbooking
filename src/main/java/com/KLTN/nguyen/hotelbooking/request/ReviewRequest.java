package com.KLTN.nguyen.hotelbooking.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private int star;
    private String comment;
    private Long hotelId;
}
