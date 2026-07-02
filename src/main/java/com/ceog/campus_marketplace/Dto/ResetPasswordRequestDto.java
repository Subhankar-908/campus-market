package com.ceog.campus_marketplace.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDto {
    @NotBlank
    private String mobile;
    @NotBlank private String otp;
    @NotBlank @Size(min=6) private String newPassword;
}