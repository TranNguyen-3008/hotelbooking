package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.HotelAttributeRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.HotelPhotoRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelAttributeResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelPhotoResponse;
import com.KLTN.nguyen.hotelbooking.entity.Attribute;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.HotelAttributes;
import com.KLTN.nguyen.hotelbooking.entity.HotelPhoto;
import com.KLTN.nguyen.hotelbooking.mapper.HotelAttributeMapper;
import com.KLTN.nguyen.hotelbooking.mapper.HotelPhotoMapper;
import com.KLTN.nguyen.hotelbooking.repository.AttributeRepository;
import com.KLTN.nguyen.hotelbooking.repository.HotelAttributeRepository;
import com.KLTN.nguyen.hotelbooking.repository.HotelPhotoRepository;
import com.KLTN.nguyen.hotelbooking.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelAttributeService {
    final AttributeRepository attributeRepository;
    final HotelAttributeRepository hotelAttributeRepository;
    final HotelRepository hotelRepository;
    final HotelPhotoRepository hotelPhotoRepository;
    @Transactional
    public List<HotelAttributeResponse> updateHotelAttributes(Integer hotelId, List<String> attributeNames) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn"));
        List<Attribute> attributes = attributeRepository.findByAttributeIn(attributeNames);
        hotelAttributeRepository.deleteByHotel(hotel);
        List<HotelAttributes> newHotelAttributes = attributes.stream()
                .map(attr -> HotelAttributes.builder()
                        .hotel(hotel)
                        .attribute(attr)
                        .build())
                .toList();
        hotelAttributeRepository.saveAll(newHotelAttributes);
        return newHotelAttributes.stream()
                .map(hotelAttr -> HotelAttributeResponse.builder()
                        .attributeName(hotelAttr.getAttribute().getAttribute())
                        .build())
                .collect(Collectors.toList());
    }
    public List<HotelAttributeResponse> getAttributesByHotelId(Integer hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() ->
                new EntityNotFoundException("Hotel not found"));
        List<HotelAttributes> hotelAttributesList = hotelAttributeRepository.findAllByHotel(hotel);
        return hotelAttributesList.stream()
                .map(hotelAttribute -> {
                    return new HotelAttributeResponse(
                            hotelAttribute.getAttribute().getId(),
                            hotelAttribute.getAttribute().getAttribute());
                })
                .collect(Collectors.toList());
    }
    public HotelPhotoResponse addPhoto(Integer hotelId, String photoUrl) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        HotelPhoto hotelPhoto = HotelPhoto.builder()
                .hotel(hotel)
                .urlPhoto(photoUrl)
                .build();
        hotelPhotoRepository.save(hotelPhoto);
        return HotelPhotoMapper.toResponseDTO(hotelPhoto);
    }
    public List<HotelPhotoResponse> getPhotosByHotelId(Integer hotelId) {
        List<HotelPhoto> photos = hotelPhotoRepository.findByHotelId(hotelId);

        return photos.stream()
                .map(HotelPhotoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    public void deletePhoto(Integer photoId) {
        hotelPhotoRepository.deleteById(photoId);
    }
    public List<Attribute> getAllAttributes(){
        return attributeRepository.findAll();
    }
}
