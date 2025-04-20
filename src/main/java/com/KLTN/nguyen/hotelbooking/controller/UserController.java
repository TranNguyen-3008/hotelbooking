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
    public ResponseEntity<Object> creatUsers(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Integer id, @RequestBody UserRequest userUpdate){
        return ResponseEntity.ok(userService.updateUser(id, userUpdate));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/lock/{id}")
    public ResponseEntity<?> lockUser(@PathVariable Integer id) {
        userService.lockUser(id);
        return ResponseEntity.ok().body("User locked successfully");
    }
    @PutMapping("/active/{id}")
    public ResponseEntity<?> activeUser(@PathVariable Integer id) {
        userService.activeUser(id);
        return ResponseEntity.ok().body("User active successfully");
    }
}
