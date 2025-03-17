package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.request.AuthenticationRequest;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.KLTN.nguyen.hotelbooking.repository.UserRepo;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;
    private final UserRepo userRepository;
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
    public String authenticateUser(AuthenticationRequest loginRequest) throws KeyLengthException {
        var user = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        boolean authenticated = passwordEncoder.matches(loginRequest.getPassWord(), user.getPassword());
        if(!authenticated)
            throw new BadCredentialsException("Wrong password");

        String token = generateToken(user);
        log.info("Authenticating user: {}", loginRequest.getUserName());
        return token;
    }
}