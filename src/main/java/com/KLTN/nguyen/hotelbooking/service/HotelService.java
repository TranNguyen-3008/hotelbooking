package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.mapper.HotelMapper;
import com.KLTN.nguyen.hotelbooking.repository.HotelRepository;
import com.KLTN.nguyen.hotelbooking.repository.ProvinceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class HotelService {
    private HotelRepository hotelRepository;
    private ProvinceRepository provinceRepository;
    public List<HotelResponse> getHotels(Integer pageNumber){
        Pageable page = PageRequest.of(pageNumber, 10);
        List<Hotel> hotels = hotelRepository.findAll(page).getContent();
        return hotels.stream().map(HotelMapper::toResponseDTO).toList();
    }
    public HotelResponse createHotel(HotelRequest hotelRequest){
        Hotel hotel = Hotel.builder()
                .hotelName(hotelRequest.getHotelName())
                .address(hotelRequest.getAddress())
                .province(provinceRepository.findAllById(hotelRequest.getProvinceId()))
                .build();
    }
}
