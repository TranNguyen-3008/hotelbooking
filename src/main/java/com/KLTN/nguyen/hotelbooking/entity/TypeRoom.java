package com.KLTN.nguyen.hotelbooking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "type_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeRoom {
    @Id
    private String code;

    private String description;

    @OneToMany(mappedBy = "typeRoom")
    private List<Room> rooms;
}
