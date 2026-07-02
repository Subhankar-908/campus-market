package com.ceog.campus_marketplace.Controller;


import com.ceog.campus_marketplace.Repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ceog.campus_marketplace.Model.SuperAdmin;

@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {
    @Autowired
    private SuperAdminRepository superAdminRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getSuperAdmin(@RequestParam Long id){
         com.ceog.campus_marketplace.Model.SuperAdmin superAdmin= superAdminRepository.findById(id).
                orElseThrow(()-> new RuntimeException("not a super Admin"));
         return ResponseEntity.ok(superAdmin);
    }
}
