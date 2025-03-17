package com.KLTN.nguyen.hotelbooking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String fullName;
    private String cccd;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean isWorking;

}
