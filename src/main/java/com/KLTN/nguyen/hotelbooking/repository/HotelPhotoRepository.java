package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.entity.HotelPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelPhotoRepository extends JpaRepository<HotelPhoto,Integer> {

    List<HotelPhoto> findByHotelId(Integer hotelId);
}
