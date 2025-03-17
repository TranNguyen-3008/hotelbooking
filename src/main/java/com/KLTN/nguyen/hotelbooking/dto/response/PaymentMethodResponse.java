package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodResponse {
    private Integer id;
    private String methodName;
}

