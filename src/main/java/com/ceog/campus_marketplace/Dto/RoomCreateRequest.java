package com.ceog.campus_marketplace.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateRequest {
    @NotBlank(message = "Room ID is required")
    private String roomId;

    @NotNull(message = "Seller ID is required")
    private Long sellerId;

    @NotNull(message = "Buyer ID is required")
    private Long buyerId;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
