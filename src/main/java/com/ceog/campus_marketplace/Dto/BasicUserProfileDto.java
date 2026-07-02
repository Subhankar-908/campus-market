package com.ceog.campus_marketplace.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicUserProfileDto {

    private Long Id;
    private String name;
    private String college;
}
