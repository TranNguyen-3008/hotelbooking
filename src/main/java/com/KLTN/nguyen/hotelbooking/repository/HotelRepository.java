package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.HotelStatus;
import com.KLTN.nguyen.hotelbooking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {
    List<Hotel> findAllByStatus(HotelStatus status, Pageable pageable);
    Hotel findByOwner(User user);
    List<Hotel> findAllByHotelNameContaining(String name, Pageable pageable);
    @Query("SELECT h FROM Hotel h " +
            "WHERE (:name IS NULL OR h.hotelName LIKE :#{#name}) " +
            "AND (:location IS NULL OR h.province.provinceName LIKE :#{#location})" +
            "AND (h.status.code = 'ACCEPT')")
    Page<Hotel> searchHotels(@Param("name") String name,
                             @Param("location") String location,
                             Pageable pageable);
    Hotel findByEmail(String email);
}
