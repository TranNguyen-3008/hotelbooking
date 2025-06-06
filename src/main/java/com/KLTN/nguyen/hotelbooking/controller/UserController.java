package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.ChangePassRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.UserRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.UserResponse;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;
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
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = authorizationHeader.substring(7); // Loại bỏ 'Bearer ' từ token
            Long userId = authenticationService.getUserIdFromToken(token);
            Integer id = userId.intValue();
            UserResponse user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Trả về BadRequest nếu có lỗi
        }
    }
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserRequest userUpdate){
        try {
            String token = authorizationHeader.substring(7);
            Long userId = authenticationService.getUserIdFromToken(token);
            Integer id = userId.intValue();
            return ResponseEntity.ok(userService.updateUser(id, userUpdate));
        }catch (Exception e){
            return ResponseEntity.status(400).body(null);
        }
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
    @PutMapping("/change-password")
    public ResponseEntity<UserResponse> changePassword(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody ChangePassRequest changePassRequest){
        try {
            String token = authorizationHeader.substring(7);
            Long userId = authenticationService.getUserIdFromToken(token);
            Integer id = userId.intValue();
            return ResponseEntity.ok(userService.changePassword(id, changePassRequest.getOldPassword(), changePassRequest.getNewPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Trả về BadRequest nếu có lỗi
        }
    }
}
