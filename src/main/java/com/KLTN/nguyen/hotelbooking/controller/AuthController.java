package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.request.AuthenticationRequest;
import com.KLTN.nguyen.hotelbooking.response.AuthenticationResponse;
import com.KLTN.nguyen.hotelbooking.response.UserResponse;
import com.KLTN.nguyen.hotelbooking.service.UserService;
import com.nimbusds.jose.KeyLengthException;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Object> register(@RequestBody User request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.ok(new String("User already"));
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request) throws KeyLengthException {
        String token = userService.authenticateUser(request);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
