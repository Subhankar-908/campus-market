package com.ceog.campus_marketplace.Service;

import com.ceog.campus_marketplace.Dto.*;

import com.ceog.campus_marketplace.Model.Admin;
import com.ceog.campus_marketplace.Model.RefreshToken;
import com.ceog.campus_marketplace.Model.SuperAdmin;
import com.ceog.campus_marketplace.Model.Type.RolesType;
import com.ceog.campus_marketplace.Model.User;
import com.ceog.campus_marketplace.Repository.AdminRepository;
import com.ceog.campus_marketplace.Repository.SuperAdminRepository;
import com.ceog.campus_marketplace.Repository.UserRepository;
import com.ceog.campus_marketplace.WebSecurity.JwtUntil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUntil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;




   @Autowired
   private OtpService otpService;

   @Autowired
   private SuperAdminRepository superAdminRepository;



    @Transactional
    public LoginResponsDto login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        User user         = (User) authentication.getPrincipal();
        String accessToken  = jwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        Set<String> roleNames = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new LoginResponsDto(
                user.getId(),
                user.getUsername(),
                "Login successful",
                accessToken,
                refreshToken.getToken() ,  // send opaque refresh token to client
                roleNames
        );
    }
    @Transactional
    public TokenRefreshResponseDto refreshAccessToken(String refreshTokenStr) {
        RefreshToken rt = refreshTokenService.verifyRefreshToken(refreshTokenStr);
        User user = rt.getUser();
        String newAccessToken = jwtUtil.generateToken(user);
        return new TokenRefreshResponseDto(newAccessToken, rt.getToken(), "Bearer");
    }


    //sign-in
    @Transactional
    public SignUpResponsDto signUp(SignupRequestDto dto){
        //check user exist or not
        User user=userRepository.findByUsername(dto.getUsername()).orElse(null);
        if(user!=null){
            throw new RuntimeException( "User already exist");
        }

        //create object of new user
        user=userRepository.save(
                User.builder()
                        .username(dto.getUsername())
                        .college(dto.getCollege())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .roles(new java.util.HashSet<>(Set.of(RolesType.USER)))
                        .mobile(dto.getMobile())
                         .build());

        return new SignUpResponsDto(user.getId(),user.getUsername());
    }



    public String sendForgotPasswordOtp(String mobile) {
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("No account found with this mobile number"));
        String otp = otpService.generateOtp(mobile);
        // TODO: integrate real SMS gateway here. For now, log it (dev only).
        System.out.println("OTP for " + mobile + " is: " + otp);
        return otp; // returned only in dev — remove in production once SMS is wired up
    }

    public boolean verifyOtp(String mobile, String otp) {
        return otpService.verifyOtp(mobile, otp);
    }

    @Transactional
    public String resetPassword(String mobile, String otp, String newPassword) {
        if (!otpService.verifyOtp(mobile, otp)) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        otpService.clearOtp(mobile);
        return "Password reset successfully";
    }

    public ResponseEntity<?> bootstrap(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!adminRepository.existsById(user.getId())) {
            user.getRoles().add(RolesType.ADMIN);
            userRepository.save(user);

            Admin admin = Admin.builder().username(user.getUsername()).users(user).build();
            adminRepository.save(admin);
        }

        if (!superAdminRepository.existsById(user.getId())) {
            user.getRoles().add(RolesType.SUPER_ADMIN);
            userRepository.save(user);

            SuperAdmin superAdmin = SuperAdmin.builder().username(user.getUsername()).users(user).build();
            superAdminRepository.save(superAdmin);
        }

        return ResponseEntity.ok("Admin + Super admin ensured for: " + user.getUsername());
    }
    }


