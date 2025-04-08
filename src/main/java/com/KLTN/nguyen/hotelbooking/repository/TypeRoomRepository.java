package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Integer> {
    TypeRoom findByCode(String name);
}
