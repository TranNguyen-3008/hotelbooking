package com.KLTN.nguyen.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String hotelName;
    private String image;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String address;
    private String phoneNumber;
    private String email;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne
    @JoinColumn(name = "status_code")
    private HotelStatus status;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    private List<Review> reviews;

    @OneToMany(mappedBy = "hotel")
    private List<HotelPhoto> photos;

    @OneToMany(mappedBy = "hotel")
    private List<HotelAttributes> attributes;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}

