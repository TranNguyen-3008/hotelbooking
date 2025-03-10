package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.entity.UserEntity;
import com.KLTN.nguyen.hotelbooking.request.LoginRequest;
import com.KLTN.nguyen.hotelbooking.response.LoginResponse;
import com.KLTN.nguyen.hotelbooking.service.UserService;
import com.nimbusds.jose.KeyLengthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.KLTN.nguyen.hotelbooking.repository.UserRepo;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody UserEntity request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            return "Username already exists!";
        }

        UserEntity user = UserEntity.builder()
                .userName(request.getUserName())
                .passWord(passwordEncoder.encode(request.getPassWord()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return "User registered successfully!";
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authentication(@RequestBody LoginRequest request) throws KeyLengthException {
        String token = userService.authenticateUser(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
