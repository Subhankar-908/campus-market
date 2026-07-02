package com.ceog.campus_marketplace.Service;

import com.ceog.campus_marketplace.Dto.CategoryDto;
import com.ceog.campus_marketplace.Model.Category;
import com.ceog.campus_marketplace.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.catalog.Catalog;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    //----------------add category----------------------
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String addCategory(List<Category> catalog) {

        List<Category> categoryList = catalog.stream()
                .map(c -> Category.builder()
                        .name(c.getName())
                        .build()
                )
                .toList();

        categoryRepository.saveAll(categoryList);
        return "category add successfully";
    }

    //--------------------delete category---------------
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public void deleteCategory(String categoryName){

        Category category=categoryRepository.findByName(categoryName)
                .orElseThrow(()-> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    //get all product category
//    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryDto> allCategory(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(cat -> new CategoryDto(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
    }
}
