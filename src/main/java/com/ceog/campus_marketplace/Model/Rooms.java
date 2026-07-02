package com.ceog.campus_marketplace.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;
    private Long sellerId;    // ← add this
    private Long buyerId;     // ← add this
    private Long productId;

    @OneToMany(
            mappedBy="room",
            cascade=CascadeType.ALL,
            fetch=FetchType.EAGER,
            orphanRemoval=true
    )
    List<Messages> messages=new ArrayList<>();
}
