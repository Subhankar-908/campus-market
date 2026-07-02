package com.ceog.campus_marketplace.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String chat;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name="room_id")
    @JsonIgnore
    private Rooms room;

    public Messages(String sender, String chat) {
        this.sender = sender;
        this.chat = chat;
        this.time = LocalDateTime.now();
    }
}
