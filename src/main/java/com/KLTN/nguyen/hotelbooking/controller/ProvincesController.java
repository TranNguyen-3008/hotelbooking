package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.entity.Province;
import com.KLTN.nguyen.hotelbooking.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class ProvincesController {
    private final ProvinceRepository provinceRepository;
    @GetMapping
    public List<Province> getProvinces(){
        return provinceRepository.findAll();
    }
}
