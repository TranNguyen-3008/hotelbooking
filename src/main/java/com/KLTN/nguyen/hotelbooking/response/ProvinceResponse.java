package com.KLTN.nguyen.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvinceResponse {
    private Long id;
    private String provinceName;
}
