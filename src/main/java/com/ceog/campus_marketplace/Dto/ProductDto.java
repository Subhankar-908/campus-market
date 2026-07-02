package com.ceog.campus_marketplace.Dto;

import com.ceog.campus_marketplace.Model.Category;
import com.ceog.campus_marketplace.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String title;
    private String description;
    private String amount;
    private String imgUrl;
    private Long sellerId;
    private String sellerName;
    private LocalDateTime uploadDate;
    private Long categoryId;
    private String categoryName;
    private String status;
    private Long BuyerId;
    private String college;
}
