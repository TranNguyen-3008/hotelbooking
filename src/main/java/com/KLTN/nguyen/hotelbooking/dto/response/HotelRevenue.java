package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HotelRevenue {
    private LocalDate date;
    private Double revenue;
}