package com.ceog.campus_marketplace.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponsDto {
    private Long id;
    private String username;
    private String status;
    private String jwt;
//    private String refreshToken;
    private Set<String> roles;
}
