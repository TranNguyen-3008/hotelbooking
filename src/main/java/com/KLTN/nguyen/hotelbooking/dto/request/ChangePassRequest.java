package com.KLTN.nguyen.hotelbooking.dto.request;

import lombok.Data;

@Data
public class ChangePassRequest {
    private String oldPassword;
    private String newPassword;
}
