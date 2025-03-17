package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeRoomResponse {
    private String code;
    private String description;
}
