package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Page<Room> findAllByHotel(Hotel hotel, Pageable pageable);
}
