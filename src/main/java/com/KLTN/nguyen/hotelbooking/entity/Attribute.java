package com.KLTN.nguyen.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String attribute;

    @OneToMany(mappedBy = "attribute")
    private List<HotelAttributes> hotelAttributes;
}
