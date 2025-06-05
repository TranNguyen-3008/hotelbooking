package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.dto.response.HotelRevenue;
import com.KLTN.nguyen.hotelbooking.entity.Booking;
import com.KLTN.nguyen.hotelbooking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByUser(User user, Pageable pageable);
    @Query("SELECT b FROM Booking b WHERE b.room.hotel.id = :hotelId")
    Page<Booking> findBookingsByHotelId(@Param("hotelId") Integer hotelId, Pageable pageable);
    @Query("SELECT b FROM Booking b WHERE b.room.hotel.id = :hotelId AND b.status.status = :status")
    Page<Booking> findByHotelIdAndStatus(@Param("hotelId") Integer hotelId,@Param("status") String status, Pageable pageable);
    @Query("SELECT b FROM Booking b WHERE b.room.hotel.id = :hotelId " +
            "AND b.startDate < :endDate AND b.endDate > :startDate")
    List<Booking> findBookingsInDateRange(@Param("hotelId") Integer hotelId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("""
    SELECT new com.KLTN.nguyen.hotelbooking.dto.response.HotelRevenue(
        b.endDate,
        SUM(b.totalPrice)
    )
    FROM Booking b
    WHERE b.room.hotel.id = :hotelId
      AND b.status.code = 'DONE'
      AND (:startDate IS NULL OR b.endDate >= :startDate)
      AND (:endDate IS NULL OR b.endDate <= :endDate)
    GROUP BY b.endDate
    ORDER BY b.endDate
""")
    List<HotelRevenue> getRevenueByEndDateFiltered(
            @Param("hotelId") Integer hotelId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    long countByBookingDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.bookingDate BETWEEN :start AND :end")
    long calculateTotalRevenue(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
