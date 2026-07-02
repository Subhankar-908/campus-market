package com.ceog.campus_marketplace.Controller;


import com.ceog.campus_marketplace.Dto.CategoryDto;
import com.ceog.campus_marketplace.Model.Category;
import com.ceog.campus_marketplace.Service.CategoryService;
import com.ceog.campus_marketplace.WebSecurity.JwtUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorys")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtUntil jwtUntil;

    //add category
    @PostMapping("/addCategory")
    public ResponseEntity<String> addCategory(@RequestBody List<Category> category){

            return ResponseEntity.ok(categoryService.addCategory(category));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(
            @RequestParam("CategorName") String categoryName){

            categoryService.deleteCategory(categoryName.trim());
            return ResponseEntity.ok("Category delete successfully");


    }

    @GetMapping("/allCategory")
    public ResponseEntity<List<CategoryDto>> getAllCategory(){

            return ResponseEntity.ok(categoryService.allCategory());

    }

}
