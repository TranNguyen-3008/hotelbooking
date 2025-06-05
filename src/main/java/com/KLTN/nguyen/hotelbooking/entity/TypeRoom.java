package com.KLTN.nguyen.hotelbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "typeRoom")
    private List<Room> rooms;
}
