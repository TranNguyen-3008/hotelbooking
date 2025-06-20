package com.KLTN.nguyen.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String fullName;
    private String password;
    private String cccd;
    private String email;
    private String phoneNumber;
    private String role;
    @Column(name = "is_working")
    private Boolean isWorking;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}