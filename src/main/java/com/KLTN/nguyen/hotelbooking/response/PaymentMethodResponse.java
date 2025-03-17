package com.KLTN.nguyen.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodResponse {
    private Long id;
    private String methodName;
}

