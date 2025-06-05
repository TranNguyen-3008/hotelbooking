package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatsResponse {
    private long revenue;
    private long bookings;
    private long users;
    private long hotelOwners;
}
