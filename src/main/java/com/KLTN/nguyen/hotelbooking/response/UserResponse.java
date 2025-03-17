package com.KLTN.nguyen.hotelbooking.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String cccd;
    private String email;
    private String phoneNumber;
    private String role;
}
