package com.ceog.campus_marketplace.Service;

import com.ceog.campus_marketplace.Dto.RoomCreateRequest;
import com.ceog.campus_marketplace.Model.Rooms;
import com.ceog.campus_marketplace.Repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomsRepository roomsRepository;

    //findRoomsBySellerId
    public List<Rooms> findRoomsBySellerId(Long sellerId){
        List<Rooms> AllRooms=roomsRepository.findByRoomIdContaining("product_");

        List<Rooms> sellerRoom=AllRooms.stream()
                .filter(r->r.getSellerId()!=null &&
                        r.getSellerId().equals(sellerId))
                .collect(Collectors.toList());
        return sellerRoom;
    }



    public List<Rooms> findRoomsByBuyerId(Long buyerId){
        List<Rooms> AllRooms = roomsRepository.findByRoomIdContaining("product_");

        List<Rooms> BuyerRoom = AllRooms.stream()
                .filter(r -> r.getBuyerId() != null &&
                        r.getBuyerId().equals(buyerId))
                .collect(Collectors.toList());
        return BuyerRoom;
    }



    //create room
    @Transactional
    public Rooms createRoom(RoomCreateRequest request){


        Rooms room = new Rooms();
        room.setRoomId(request.getRoomId());
        room.setSellerId(request.getSellerId());
        room.setBuyerId(request.getBuyerId());
        room.setProductId(request.getProductId());
        roomsRepository.save(room);
        return room;
    }

}
