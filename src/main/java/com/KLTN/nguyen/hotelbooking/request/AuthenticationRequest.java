package com.KLTN.nguyen.hotelbooking.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String userName;
    private String passWord;
}
