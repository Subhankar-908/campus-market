package com.ceog.campus_marketplace.Repository;

import com.ceog.campus_marketplace.Model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    Rooms findByRoomId(String roomId);

    List<Rooms> findByRoomIdContaining(String product);
//    List<Rooms> findRoomsBySellerId(Long sellerId);
}