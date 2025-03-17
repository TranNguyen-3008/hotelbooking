package com.KLTN.nguyen.hotelbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "booking_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatus {
    @Id
    private String code;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String status;
}
