package com.ceog.campus_marketplace.Dto;

import com.ceog.campus_marketplace.Model.Type.RolesType;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "password is requered")
    @Size(min=6,max=100,message = "password must be at least 6 characters")
    private String password;

    @NotBlank(message = "College name is required")
    @Size(min = 2, max = 100, message = "College name must be between 2 and 100 characters")
    private String college;   // ← NEW

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
    private String mobile;


}
