package com.KLTN.nguyen.hotelbooking.mapper;

import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.dto.request.UserRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public static User toEntity(UserRequest dto) {
        return User.builder()
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .password(dto.getPassword())
                .cccd(dto.getCccd())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole())
                .build();
    }

    public static UserResponse toResponseDTO(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .cccd(user.getCccd())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isWorking(user.getIsWorking())
                .build();
    }
}
