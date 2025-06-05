package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String username;
    private String fullName;
    private String password;
    private String cccd;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean isWorking;
}
