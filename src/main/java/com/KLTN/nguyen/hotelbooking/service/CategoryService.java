package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.CategoryRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.TypeRoomRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.CategoryResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.TypeRoomResponse;
import com.KLTN.nguyen.hotelbooking.entity.Attribute;
import com.KLTN.nguyen.hotelbooking.entity.Province;
import com.KLTN.nguyen.hotelbooking.entity.TypeRoom;
import com.KLTN.nguyen.hotelbooking.repository.AttributeRepository;
import com.KLTN.nguyen.hotelbooking.repository.ProvinceRepository;
import com.KLTN.nguyen.hotelbooking.repository.TypeRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ProvinceRepository provinceRepository;
    private final TypeRoomRepository typeRoomRepository;
    private final AttributeRepository attributeRepository;
    public void addProvince(CategoryRequest categoryRequest){
        Province province = Province.builder()
                .provinceName(categoryRequest.getName())
                .build();
        provinceRepository.save(province);
    }
    public void addTypeRoom(TypeRoomRequest typeRoomRequest){
        TypeRoom typeRoom = TypeRoom.builder()
                .code(typeRoomRequest.getCode())
                .description(typeRoomRequest.getDescription())
                .build();
        typeRoomRepository.save(typeRoom);
    }
    public void addAttribute(CategoryRequest categoryRequest){
        Attribute attribute = Attribute.builder()
                .attribute(categoryRequest.getName())
                .build();
        attributeRepository.save(attribute);
    }

    public List<CategoryResponse> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return provinces.stream()
                .map(p -> {
                    CategoryResponse res = new CategoryResponse();
                    res.setName(p.getProvinceName());
                    res.setType("area");
                    return res;
                }).toList();
    }

    public List<CategoryResponse> getAllAttributes() {
        List<Attribute> attributes = attributeRepository.findAll();
        return attributes.stream()
                .map(p -> {
                    CategoryResponse res = new CategoryResponse();
                    res.setName(p.getAttribute());
                    res.setType("area");
                    return res;
                }).toList();
    }

    public List<TypeRoomResponse> getAllRoomTypes() {
        List<TypeRoom> roomTypes = typeRoomRepository.findAll();
        return roomTypes.stream()
                .map(rt -> {
                    return TypeRoomResponse.builder()
                            .code(rt.getCode())
                            .description(rt.getDescription())
                            .build();
                }).toList();
    }

}
