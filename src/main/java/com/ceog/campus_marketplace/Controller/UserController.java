package com.ceog.campus_marketplace.Controller;

import com.ceog.campus_marketplace.Dto.*;
import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Service.UserService;
import com.ceog.campus_marketplace.WebSecurity.JwtUntil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUntil jwtUntil;


    //get All user
    @GetMapping("/allUser")
    public ResponseEntity<Page<BasicUserProfileDto>> getAllUser(

            @RequestParam(defaultValue = "0")          int page,
            @RequestParam(defaultValue = "20")         int size,
            @RequestParam(defaultValue = "uploadDate") String sortBy,
            @RequestParam(defaultValue = "desc")       String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, Math.min(size, 100), sort);

        return ResponseEntity.ok(userService.getAllUser(pageable));
}

    @PutMapping("/updateProfile/{id}")
    public ResponseEntity<String> updateProfile(
            @PathVariable Long id,
            @RequestBody updateProfileDto update) {

        String result = userService.profileDetailsUpdate(id, update);
        if ("Server error".equals(result)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }

    //get User By id
    @GetMapping("/userId/{userId}")
    public ResponseEntity<AdminUserDetailDto> getUserByIdUserAdmin(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserByIdUserAdmin(userId));
    }

    @GetMapping("/profile")
    public ResponseEntity<BasicUserProfileDto> getUserById() {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BasicUserProfileDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String college,
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "20")   int size) {

        Pageable pageable = PageRequest.of(page, Math.min(size, 100),
                Sort.by("username").ascending());

        return ResponseEntity.ok(
                userService.getUserByNameAndCollege(name, college, pageable));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            String result = userService.changePassword(user.getId(), dto);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //delete user
    @DeleteMapping("/deleteUser")
    public ResponseEntity<DeleteUserResponse> deleteUser(
            @RequestBody User user) {

        User currentUser =
                (User) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        return ResponseEntity.ok(
                userService.deleteUser(currentUser.getId(), user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id
    ){
        User currentUser =
                (User) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
        return ResponseEntity.ok(userService.deleteUser(id, currentUser.getId()));

    }

}
