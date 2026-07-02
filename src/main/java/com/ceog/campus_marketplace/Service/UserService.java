package com.ceog.campus_marketplace.Service;

import com.ceog.campus_marketplace.Dto.*;
import com.ceog.campus_marketplace.Model.Product;
import com.ceog.campus_marketplace.Model.Type.RolesType;
import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Repository.AdminRepository;
import com.ceog.campus_marketplace.Repository.UserRepository;
import com.ceog.campus_marketplace.WebSecurity.JwtUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUntil jwtUntil;


    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public Page<BasicUserProfileDto> getUserByNameAndCollege(
            String name,
            String college,
            Pageable pageable) {

        // both provided → filter by both
        if (StringUtils.hasText(name) && StringUtils.hasText(college)) {
            return userRepository
                    .findByUsernameContainingIgnoreCaseAndCollegeIgnoreCase(name, college, pageable)
                    .map(u -> BasicUserProfileDto.builder()
                            .Id(u.getId())
                            .name(u.getUsername())
                            .college(u.getCollege())
                            .build());
        }

        // only name
        if (StringUtils.hasText(name)) {
            return userRepository
                    .findByUsernameContainingIgnoreCase(name, pageable)
                    .map(u -> BasicUserProfileDto.builder()
                            .Id(u.getId())
                            .name(u.getUsername())
                            .college(u.getCollege())
                            .build());
        }

        // only college
        if (StringUtils.hasText(college)) {
            return userRepository
                    .findByCollegeIgnoreCase(college, pageable)
                    .map(u -> BasicUserProfileDto.builder()
                            .Id(u.getId())
                            .name(u.getUsername())
                            .college(u.getCollege())
                            .build());
        }

        // neither → return all users
        return userRepository.findAll(pageable)
                .map(u -> BasicUserProfileDto.builder()
                        .Id(u.getId())
                        .name(u.getUsername())
                        .college(u.getCollege())
                        .build());
    }

    //getAllUser
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public Page<BasicUserProfileDto> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> BasicUserProfileDto.builder()
                        .Id(user.getId())
                        .name(user.getUsername())
                        .college(user.getCollege())
                        .build());
    }

    //----------------update profile---------------
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public String profileDetailsUpdate(Long id,updateProfileDto update){
        try{
            User user=userRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("user not founde"));
            if(user==null)return "Server error";
                    user.setUsername(update.getUsername());
                    user.setCollege(update.getCollege());
                    userRepository.save(user);
            return "Update Successfully completed";
        } catch (RuntimeException e) {
            throw new RuntimeException("server error");
        }
    }

    //get user by id
    @PreAuthorize("hasRole('USER')")
    public BasicUserProfileDto getUserById(Long userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("user not found"));
        BasicUserProfileDto dto=new BasicUserProfileDto();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setCollege(user.getCollege());
        return dto;
    }

    // ── GET USER BY ID (ADMIN) — full details ────────────────────────────
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public AdminUserDetailDto getUserByIdUserAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Map products
        List<ProductDto> productDtos = user.getProducts() == null
                ? List.of()
                : user.getProducts().stream()
                .map(p -> ProductDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .amount(p.getAmount())
                        .imgUrl(p.getImgUrl())
                        .status(p.getStatus())
                        .uploadDate(p.getUploadDate())
                        .college(p.getCollege())
                        .sellerId(user.getId())
                        .sellerName(user.getUsername())
                        .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                        .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                        .BuyerId(p.getBuyId())
                        .build()
                ).collect(Collectors.toList());

        long active = productDtos.stream()
                .filter(p -> "AVAILABLE".equals(p.getStatus())).count();
        long sold = productDtos.stream()
                .filter(p -> "OUT_OF_STOCK".equals(p.getStatus())).count();

        // Map reports made by this user
        List<ReportDto> reportsMade = user.getReportsMade() == null
                ? List.of()
                : user.getReportsMade().stream()
                .map(r -> ReportDto.builder()
                        .id(r.getId())
                        .reason(r.getReason())
                        .reportDate(r.getReportDate())
                        .reporterId(r.getReporter() != null ? r.getReporter().getId() : null)
                        .repoterName(r.getReporter() != null ? r.getReporter().getUsername() : null)
                        .reportedUserId(r.getReportedUser() != null ? r.getReportedUser().getId() : null)
                        .reportedUserName(r.getReportedUser() != null ? r.getReportedUser().getUsername() : null)
                        .productTitle(r.getProductTitle())
                        .productTitle(r.getProductTitle())
                        .build()
                ).collect(Collectors.toList());

        // Map reports received by this user
        List<ReportDto> reportsReceived = user.getReportReceive() == null
                ? List.of()
                : user.getReportReceive().stream()
                .map(r -> ReportDto.builder()
                        .id(r.getId())
                        .reason(r.getReason())
                        .reportDate(r.getReportDate())
                        .reporterId(r.getReporter() != null ? r.getReporter().getId() : null)
                        .repoterName(r.getReporter() != null ? r.getReporter().getUsername() : null)
                        .reportedUserId(r.getReportedUser() != null ? r.getReportedUser().getId() : null)
                        .reportedUserName(r.getReportedUser() != null ? r.getReportedUser().getUsername() : null)
                        .productTitle(r.getProductTitle())
                        .productTitle(r.getProductTitle())
                        .build()
                ).collect(Collectors.toList());

        return AdminUserDetailDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .college(user.getCollege())
                .roles(user.getRoles())
                .totalListings(productDtos.size())
                .activeListings((int) active)
                .soldListings((int) sold)
                .products(productDtos)
                .reportsMadeCount(reportsMade.size())
                .reportsReceivedCount(reportsReceived.size())
                .reportsMade(reportsMade)
                .reportsReceived(reportsReceived)
                .build();
    }
    //------------------delete user---------------
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public DeleteUserResponse deleteUser(Long userId, User user1) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        System.out.println("received password: " + user1.getPassword());
        System.out.println("user password: " + user.getPassword());

        if (encoder.matches(user1.getPassword(), user.getPassword())) {
            adminRepository.deleteByUsers(user);
            userRepository.delete(user);

            return DeleteUserResponse.builder()
                    .success(true)
                    .message("User deleted successfully")
                    .deletedUserId(userId)
                    .time(LocalDateTime.now())
                    .build();
        }

        return DeleteUserResponse.builder()
                .success(false)
                .message("User not deleted (password mismatch)")
                .deletedUserId(null)
                .time(LocalDateTime.now())
                .build();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public String changePassword(Long userId, ChangePasswordDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (dto.getCurrentPassword().equals(dto.getNewPassword())) {
            throw new RuntimeException("New password must be different from current password");
        }

        user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully";
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public DeleteUserResponse deleteUser(
            Long userId,
            Long AdminId

    ) {


        User currentUser =
                userRepository.findById(AdminId).orElseThrow(()->new RuntimeException("login user not found"));



        User targetUser =
                userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("user not found"));

        Set<RolesType> currentRoles =
                currentUser.getRoles();
        Set<RolesType> targetRoles =
                targetUser.getRoles();
        boolean isAdmin = false;
        boolean isSuperAdmin = false;
        // check current user roles
        for(RolesType role : currentRoles)
        {

            if(role == RolesType.ADMIN)
            {
                isAdmin = true;
            }


            if(role == RolesType.SUPER_ADMIN)
            {
                isSuperAdmin = true;
            }

        }
        boolean targetIsUser = false;
        boolean targetIsAdmin = false;
        boolean targetIsSuperAdmin = false;



        // check target user roles
        for(RolesType role : targetRoles)
        {

            if(role == RolesType.USER) {
                targetIsUser = true;
            }
            if(role == RolesType.ADMIN)
            {
                targetIsAdmin = true;
            }
            if(role == RolesType.SUPER_ADMIN)
            {
                targetIsSuperAdmin = true;
            }

        }
        // ADMIN delete only USER
        if(isAdmin && !isSuperAdmin)
        {
            if(!targetIsUser)
            {
                throw new RuntimeException(
                        "ADMIN can delete only USER"
                );
            }
        }
        // SUPER_ADMIN delete USER + ADMIN
        if(isSuperAdmin)
        {

            if(targetIsSuperAdmin)
            {
                throw new RuntimeException(
                        "Cannot delete SUPER_ADMIN"
                );
            }

        }
        adminRepository.deleteByUsers(targetUser);
        userRepository.delete(targetUser);
        return DeleteUserResponse.builder()
                .success(true)
                .message("Deleted successfully")
                .deletedUserId(userId)
                .time(LocalDateTime.now())
                .build();

    }
}
