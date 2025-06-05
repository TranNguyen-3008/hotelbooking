package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MomoPaymentRequest {
    private String startDate;
    private String endDate;
    private Long roomId;
    private String methodName; // "online"
    private String email;
    private BigDecimal price;
    private String orderId;
    private String orderInfo;
}
