package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.HotelAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelAttributeRepository extends JpaRepository<HotelAttributes, Integer> {
    List<HotelAttributes> findAllByHotel(Hotel hotel);
}
