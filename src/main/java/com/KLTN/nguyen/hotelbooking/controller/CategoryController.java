package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.CategoryRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.TypeRoomRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.CategoryResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.TypeRoomResponse;
import com.KLTN.nguyen.hotelbooking.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{type}")
    public ResponseEntity<List<?>> getCategoriesByType(@PathVariable String type) {
        List<?> list;
        switch(type.toLowerCase()) {
            case "area":
                list = categoryService.getAllProvinces();
                break;
            case "facility":
                list = categoryService.getAllAttributes();
                break;
            case "roomtype":
                list = categoryService.getAllRoomTypes();
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }
    @PostMapping
    public void createCategory(@RequestBody CategoryRequest request) {
        if (request.getType().equals("area")){
            categoryService.addProvince(request);
        }
        if (request.getType().equals("facility")){
            categoryService.addAttribute(request);
        }
    }
    @PostMapping("/ca")
    public void createTypeRoom(@RequestBody TypeRoomRequest typeRoomResponse) {
        categoryService.addTypeRoom(typeRoomResponse);
    }
    @PutMapping()
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryRequest request) {
        switch (request.getType().toLowerCase()) {
            case "area":
                categoryService.updateProvince(request);
                break;
            case "facility":
                categoryService.updateAttribute(request);
                break;
            case "roomtype":
                categoryService.updateRoomType(request);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}