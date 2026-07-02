package com.ceog.campus_marketplace.Controller;

import com.ceog.campus_marketplace.Dto.AdminRequestDto;
import com.ceog.campus_marketplace.Dto.AdminResponsDto;
import com.ceog.campus_marketplace.Service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/onBordNewAdmin")
    public AdminResponsDto onBordNewAdmin(@Valid @RequestBody AdminRequestDto adminRequestDto) throws IllegalAccessException {
        return adminService.onBordNewAdmin(adminRequestDto);
    }
}

