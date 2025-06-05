package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class HotelAttributesUpdateRequest {
    private List<String> attributes;
}
