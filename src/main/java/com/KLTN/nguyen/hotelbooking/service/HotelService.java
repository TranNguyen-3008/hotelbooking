package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.EditHotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.HotelRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.UserRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.UserResponse;
import com.KLTN.nguyen.hotelbooking.entity.*;
import com.KLTN.nguyen.hotelbooking.mapper.HotelMapper;
import com.KLTN.nguyen.hotelbooking.mapper.UserMapper;
import com.KLTN.nguyen.hotelbooking.repository.HotelRepository;
import com.KLTN.nguyen.hotelbooking.repository.ProvinceRepository;
import com.KLTN.nguyen.hotelbooking.repository.HotelStatusRepository;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;
import com.KLTN.nguyen.hotelbooking.util.Status;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HotelService {
    private final HotelStatusRepository hotelStatusRepository;
    private final HotelRepository hotelRepository;
    private final ProvinceRepository provinceRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    public Page<HotelResponse> searchHotels(String name, String location, int page, int size) {
        String searchName = (name == null || name.trim().isEmpty()) ? null : "%" + name.trim() + "%";
        String searchLocation = (location == null || location.trim().isEmpty()) ? null : "%" + location.trim() + "%";

        Page<Hotel> hotelPage = hotelRepository.searchHotels(
                searchName,
                searchLocation,
                PageRequest.of(page, size)
        );
        return hotelPage.map(HotelMapper::toResponseDTO);
    }
    public void createHotel(HotelRequest hotelRequest, String image) {
        Province province = provinceRepository.findById(hotelRequest.getProvinceId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tỉnh/thành"));
        if (hotelRepository.findByEmail(hotelRequest.getEmail()) != null) {
            throw new EntityExistsException("Email đã được sử dụng cho khách sạn khác");
        }
        if(userRepository.findByEmail(hotelRequest.getEmail()).isPresent()){
            throw new EntityExistsException("Email đã sử dụng");
        }
        User owner = userRepository.findById(1).orElseThrow();
        Hotel hotel = Hotel.builder()
                .hotelName(hotelRequest.getHotelName())
                .address(hotelRequest.getAddress())
                .province(province)
                .email(hotelRequest.getEmail())
                .owner(owner)
                .phoneNumber(hotelRequest.getPhoneNumber())
                .image(image)
                .status(hotelStatusRepository.findByCode(Status.PENDING.name()))
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);
    }
    public HotelResponse updateHotel(Integer id, EditHotelRequest hotelRequest){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Hotel isn't valid"));
        hotel.setHotelName(hotelRequest.getHotelName());
        hotel.setAddress(hotelRequest.getAddress());
        hotel.setEmail(hotelRequest.getEmail());
        hotel.setPhoneNumber(hotelRequest.getPhoneNumber());
        hotelRepository.save(hotel);
        return HotelMapper.toResponseDTO(hotel);
    }
    public HotelResponse activateHotel(Integer id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Hotel isn't valid"));
        User user = userService.createUserOnlyEmail(hotel.getEmail());
        user.setRole("OWNER");
        user.setIsWorking(Boolean.TRUE);
        userRepository.save(user);
        hotel.setOwner(user);
        hotel.setStatus(hotelStatusRepository.findByCode(Status.ACCEPT.name()));
        hotelRepository.save(hotel);
        return HotelMapper.toResponseDTO(hotel);
    }
    public HotelResponse unHideHotel(Integer id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Hotel isn't valid"));
        hotel.setStatus(hotelStatusRepository.findByCode(Status.ACCEPT.name()));
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
    public HotelResponse getHotelByHotelOwner(Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("User not found"));
        Hotel hotel = hotelRepository.findByOwner(user);
        return HotelMapper.toResponseDTO(hotel);
    }
    public HotelResponse getHotelById(Integer id){
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Not found"));
        return HotelMapper.toResponseDTO(hotel);
    }
    public List<HotelResponse> searchHotels(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return hotelRepository.findAllByHotelNameContaining(keyword, pageable)
                .stream().map(HotelMapper::toResponseDTO).toList();
    }
    public List<HotelResponse> getHotelss(Integer pageNumber){
        Pageable page = PageRequest.of(pageNumber, 10);
        List<Hotel> hotels = hotelRepository.findAll(page).getContent();
        return hotels.stream().map(HotelMapper::toResponseDTO).toList();
    }
}


