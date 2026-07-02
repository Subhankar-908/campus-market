package com.ceog.campus_marketplace.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Entity
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String amount;
    @Column(columnDefinition = "TEXT")   // ← replaces default VARCHAR(255)
    private String imgUrl;
    private LocalDateTime uploadDate;
    private Long buyId;
    private String status;
    private String college;

    @ManyToOne
    @JoinColumn(name="seller_id")
//    @JsonIgnoreProperties({"products", "seller","reportsMade", "reportReceive"})  // Ignore User's lists to prevent cycles
    @JsonIgnore  // Completely ignore seller during JSON serialization
    private User seller;

    @ManyToOne
//    @JsonIgnoreProperties("productList")
    @JsonBackReference
    private Category category;
}
