package com.ceog.campus_marketplace.Dto;

import com.ceog.campus_marketplace.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {
    private Long id;
    private String reason;
    private Long reporterId;
    private String repoterName;
    private Long reportedUserId;
    private String reportedUserName;
    private String productTitle;
    private LocalDateTime reportDate;
}
