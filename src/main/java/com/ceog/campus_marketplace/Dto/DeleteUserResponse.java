package com.ceog.campus_marketplace.Dto;


import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteUserResponse {
    private boolean success;
    private String message;
    private Long deletedUserId;
    private LocalDateTime time;
}
