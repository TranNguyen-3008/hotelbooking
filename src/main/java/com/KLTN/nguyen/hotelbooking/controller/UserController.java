package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.UserRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.UserResponse;
import com.KLTN.nguyen.hotelbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber) {

        List<UserResponse> users = userService.getUsers(pageNumber);
        return ResponseEntity.ok(users);
    }
    @PostMapping("/create")
    public ResponseEntity<Object> creatUsers(@RequestParam UserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest));
    }
    @PatchMapping
    public ResponseEntity<UserResponse> updateUser(Authentication authentication, @RequestBody UserRequest userUpdate){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        int id = ((Long) jwt.getClaim("id")).intValue();
        System.out.println("isWorking in request: " + userUpdate.getIsWorking());
        return ResponseEntity.ok(userService.updateUser(id, userUpdate));
    }

}
