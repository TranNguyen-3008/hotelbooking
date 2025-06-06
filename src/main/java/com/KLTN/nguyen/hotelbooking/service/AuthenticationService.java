package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.response.AdminStatsResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.AuthenticationResponse;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.dto.request.AuthenticationRequest;
import com.KLTN.nguyen.hotelbooking.repository.BookingRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    public String generateToken(User user) throws KeyLengthException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("chekinghotel.com")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .claim("scope",user.getRole())
                .claim("id",user.getId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        try {
            JWSObject jwsObject = new JWSObject(header,payload);
            jwsObject.sign(new MACSigner(jwtSecret.getBytes()));
            return jwsObject.serialize();
        }catch(JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }

    }
    public AuthenticationResponse authenticateUser(AuthenticationRequest loginRequest) throws KeyLengthException {
        var user = userRepository.findByUsername(loginRequest.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(loginRequest.getUsernameOrEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Sai mật khẩu");
        }
        if (user.getIsWorking() == null || !user.getIsWorking()) {
            throw new EntityExistsException("Tài khoảng đang bị khóa");
        }
        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .role("SCOPE_" + user.getRole())  // Nếu bạn dùng prefix SCOPE_ trong JWT
                .build();
    }
    public Long getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getLongClaim("id"); // Lấy claim "id"
        } catch (ParseException e) {
            throw new RuntimeException("Không thể parse token", e);
        }
    }
    public AdminStatsResponse getStats(LocalDate startDate, LocalDate endDate) {
        long revenue = bookingRepository.calculateTotalRevenue(startDate, endDate);
        long bookings = bookingRepository.countByBookingDateBetween(startDate, endDate);
        long users = userRepository.countByRole("USER");
        long hotelOwners = userRepository.countByRole("OWNER");

        return new AdminStatsResponse(revenue, bookings, users, hotelOwners);
    }
}