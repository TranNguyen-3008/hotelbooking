package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.HotelStatus;
import com.KLTN.nguyen.hotelbooking.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {
    List<Hotel> findAllByStatus(HotelStatus status, Pageable pageable);
    Hotel findByOwner(User user);
    List<Hotel> findAllByHotelNameContaining(String name, Pageable pageable);
}
