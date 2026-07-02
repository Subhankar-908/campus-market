package com.ceog.campus_marketplace.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reason;
    private LocalDateTime ReportDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_id")
    @JsonIgnoreProperties({"reportsMade", "reportReceive", "products"})  // Ignore User's lists
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"reportsMade", "reportReceive", "products"})  // Ignore User's lists
    private User reportedUser;

    private Long productId;
    private  String productTitle;
}
