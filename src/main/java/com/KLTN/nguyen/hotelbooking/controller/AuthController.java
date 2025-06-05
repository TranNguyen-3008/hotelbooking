package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.UserRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.AdminStatsResponse;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.dto.request.AuthenticationRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.AuthenticationResponse;
import com.KLTN.nguyen.hotelbooking.service.AuthenticationService;
import com.KLTN.nguyen.hotelbooking.service.UserService;
import com.nimbusds.jose.KeyLengthException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRequest request) {
        Object user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request) throws KeyLengthException {
        System.out.println(request.getUsernameOrEmail());
        System.out.println(request.getPassword());
        AuthenticationResponse token = authenticationService.authenticateUser(request);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(authenticationService.getStats(startDate, endDate));
    }
}
