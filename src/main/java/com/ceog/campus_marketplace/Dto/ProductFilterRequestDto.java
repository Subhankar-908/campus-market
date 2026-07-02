package com.ceog.campus_marketplace.Dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductFilterRequestDto {
    private Long   categoryId;   // optional
    private String college;      // optional
    private String status;       // optional — defaults to AVAILABLE
    private int    page = 0;
    private int    size = 20;
    private String sortBy    = "uploadDate";
    private String direction = "desc";
}