package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private int star;
    private String comment;
}
