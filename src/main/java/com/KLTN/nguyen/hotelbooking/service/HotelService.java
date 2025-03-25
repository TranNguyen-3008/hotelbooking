package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.HotelStatus;
import com.KLTN.nguyen.hotelbooking.entity.Province;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.mapper.HotelMapper;
import com.KLTN.nguyen.hotelbooking.repository.HotelRepository;
import com.KLTN.nguyen.hotelbooking.repository.ProvinceRepository;
import com.KLTN.nguyen.hotelbooking.repository.HotelStatusRepository;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;
import com.KLTN.nguyen.hotelbooking.util.Status;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HotelService {
    private final HotelStatusRepository hotelStatusRepository;
    private final HotelRepository hotelRepository;
    private final ProvinceRepository provinceRepository;
    private final UserRepository userRepository;
    public List<HotelResponse> getHotels(Integer pageNumber){
        Pageable page = PageRequest.of(pageNumber, 10);
        List<Hotel> hotels = hotelRepository.findAll(page).getContent();
        return hotels.stream().map(HotelMapper::toResponseDTO).toList();
    }
    public HotelResponse getHotel(Integer id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Hotel isn't exist"));
        return HotelMapper.toResponseDTO(hotel);
    }
    public HotelResponse createHotel(HotelRequest hotelRequest){
        Province province = provinceRepository.findById(hotelRequest.getProvinceId())
                .orElse(new Province());
        User owner = userRepository.findById(hotelRequest.getOwnerId()).orElse( new User());
        Hotel hotel = Hotel.builder()
                .hotelName(hotelRequest.getHotelName())
                .address(hotelRequest.getAddress())
                .province(province)
                .email(hotelRequest.getEmail())
                .owner(owner)
                .phoneNumber(hotelRequest.getPhoneNumber())
                .image(hotelRequest.getImage())
                .status(hotelStatusRepository.findByCode((Status.PENDING.name())))
                .build();
        Hotel saveHotel = hotelRepository.save(hotel);
        return HotelMapper.toResponseDTO(saveHotel);
    }
    public HotelResponse updateHotel(Integer id,HotelRequest hotelRequest){
        Province province = provinceRepository.findById(hotelRequest.getProvinceId())
                .orElse(new Province());
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Hotel isn't valid"));
        hotel.setHotelName(hotelRequest.getHotelName());
        hotel.setImage(hotelRequest.getImage());
        hotel.setAddress(hotelRequest.getAddress());
        hotel.setEmail(hotelRequest.getEmail());
        hotel.setPhoneNumber(hotelRequest.getPhoneNumber());
        hotel.setProvince(province);
        hotelRepository.save(hotel);
        return HotelMapper.toResponseDTO(hotel);
    }
    public HotelResponse activateHotel(Integer id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Hotel isn't valid"));
        hotel.setStatus(hotelStatusRepository.findByCode(Status.SUCCESS.name()));
        hotelRepository.save(hotel);
        return HotelMapper.toResponseDTO(hotel);
    }
    public HotelResponse rejectHotel(Integer id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Hotel isn't valid"));
        hotel.setStatus(hotelStatusRepository.findByCode(Status.REJECT.name()));
        hotelRepository.save(hotel);
        return HotelMapper.toResponseDTO(hotel);
    }
    public HotelResponse hideHotel(Integer id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Hotel isn't valid"));
        hotel.setStatus(hotelStatusRepository.findByCode(Status.HIDING.name()));
        hotelRepository.save(hotel);
        return HotelMapper.toResponseDTO(hotel);
    }
    public List<HotelResponse> getHotelByStatus(String Status, Integer pageNumber){
        HotelStatus hotelStatus = hotelStatusRepository.findByCode(Status);
        Pageable page = PageRequest.of(pageNumber, 10);
        List<Hotel> hotels = hotelRepository.findAllByStatus(hotelStatus, page);
        return hotels.stream().map(HotelMapper::toResponseDTO).toList();
    }
}
