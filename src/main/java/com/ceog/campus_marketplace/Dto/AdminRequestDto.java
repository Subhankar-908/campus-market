package com.ceog.campus_marketplace.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "name is required")
    private String name;

}
