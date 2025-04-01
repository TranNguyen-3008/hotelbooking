package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelAttributeResponse {
    private Integer id;
    private String attributeName;
}