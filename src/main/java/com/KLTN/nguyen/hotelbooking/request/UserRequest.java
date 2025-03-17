package com.KLTN.nguyen.hotelbooking.request;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String fullName;
    private String password;
    private String cccd;
    private String email;
    private String phoneNumber;
    private String role;
}
