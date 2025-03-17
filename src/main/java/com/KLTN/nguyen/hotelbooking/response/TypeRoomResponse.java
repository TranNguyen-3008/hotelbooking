package com.KLTN.nguyen.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeRoomResponse {
    private String code;
    private String description;
}
