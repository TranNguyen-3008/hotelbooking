package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.AuthenticationRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.UserRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.ErrorResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.UserResponse;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.mapper.UserMapper;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserResponse createUser(UserRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new EntityExistsException("User already exist");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .isWorking(request.getIsWorking())
                .build();

        userRepository.save(user);
        return UserMapper.toResponseDTO(user);
    }
    public List<UserResponse> getUsers(Integer pageNumber){
        Pageable page = PageRequest.of(pageNumber, 10);
        List<User> userList = userRepository.findAll(page).getContent();
        return userList.stream().map(UserMapper::toResponseDTO).toList();
    }
    public UserResponse updateUser(Integer id, UserRequest userRequest){
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found"));
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        user.setCccd(userRequest.getCccd());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(userRequest.getRole());
        user.setIsWorking(userRequest.getIsWorking());
        userRepository.save(user);
        return UserMapper.toResponseDTO(user);
    }
    public boolean deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("User not found"));
        userRepository.deleteById(id);
        return true;
    }
    public void lockUser(Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("User not found"));
        user.setIsWorking(Boolean.FALSE);
        userRepository.save(user);
    }
    public void activeUser(Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("User not found"));
        user.setIsWorking(Boolean.TRUE);
        userRepository.save(user);
    }
    public UserResponse getUser(Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("User not found"));
        return UserMapper.toResponseDTO(user);
    }
    public User createUserOnlyEmail(String email){
        User user = User.builder()
                .email(email)
                .password(email)
                .build();
        return user;
    }
}
