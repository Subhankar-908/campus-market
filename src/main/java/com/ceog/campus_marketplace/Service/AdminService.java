    package com.ceog.campus_marketplace.Service;


    import com.ceog.campus_marketplace.Dto.AdminRequestDto;
    import com.ceog.campus_marketplace.Dto.AdminResponsDto;
    import com.ceog.campus_marketplace.Model.Admin;
    import com.ceog.campus_marketplace.Model.Type.RolesType;
    import com.ceog.campus_marketplace.Model.User;
    import com.ceog.campus_marketplace.Repository.AdminRepository;
    import com.ceog.campus_marketplace.Repository.UserRepository;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.stereotype.Service;

    @Service
    public class AdminService {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private AdminRepository adminRepository;


        @Transactional
        @PreAuthorize("hasRole('SUPER_ADMIN') ")//OR #AdminId==Authentication.principal.id")
        public AdminResponsDto onBordNewAdmin(AdminRequestDto adminRequestDto) throws IllegalAccessException {
            System.out.println("add new role");
            User user=userRepository.findByUsername(adminRequestDto.getName())
                    .orElseThrow(()->new RuntimeException( "user not found"));
            if(adminRepository.existsById(user.getId())){
                throw new IllegalAccessException("Already a Admin");
            }

            Admin admin=Admin.builder()
                    .username(user.getUsername())
                    .users(user)
                    .build();
            user.getRoles().add(RolesType.ADMIN);
            adminRepository.save(admin);
            System.out.println("add role "+user.getRoles());
            return new AdminResponsDto(user.getId(),user.getUsername(),"ROLES "+user.getRoles().toString());
        }
    }
