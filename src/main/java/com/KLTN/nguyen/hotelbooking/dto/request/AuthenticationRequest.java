package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String usernameOrEmail;
    private String password;
}
