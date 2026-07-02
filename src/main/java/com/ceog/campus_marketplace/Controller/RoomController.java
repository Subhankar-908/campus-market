package com.ceog.campus_marketplace.Controller;

import com.ceog.campus_marketplace.Dto.RoomCreateRequest;
import com.ceog.campus_marketplace.Model.Messages;
import com.ceog.campus_marketplace.Model.Rooms;
import com.ceog.campus_marketplace.Repository.RoomsRepository;
import com.ceog.campus_marketplace.Service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private RoomsRepository roomsRepository;

    public RoomController(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }
    @Autowired
    private RoomService roomService;

    //create  room
    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody RoomCreateRequest request){

        if(roomsRepository.findByRoomId(request.getRoomId())!=null){
            return ResponseEntity.badRequest().body("Room already Exist");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(request));
    }

    //get room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId){
        Rooms room=roomsRepository.findByRoomId(roomId);
        if(room==null){
            return ResponseEntity.badRequest().body("Room Not Found");
        }
        return ResponseEntity.ok(room);

    }

    //get all chat in room
    @GetMapping("/{roomId}/message")
    @Transactional
    public ResponseEntity<List<Messages>> getMessage(@PathVariable String roomId){
        Rooms room=roomsRepository.findByRoomId(roomId);
        if(room==null){
            return ResponseEntity.badRequest().build();
        }
        List<Messages> messages=room.getMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/seller/{sellerId}")
    @Transactional
    public ResponseEntity<List<Rooms>> GetRoomsBySeller(
            @PathVariable Long sellerId
    ){
        return ResponseEntity.ok(roomService.findRoomsBySellerId(sellerId));
    }

    @GetMapping("/buyer/{buyerId}")
    @Transactional
    public ResponseEntity<List<Rooms>> GetRoomsByBuyer(
            @PathVariable Long buyerId
    ){
        return ResponseEntity.ok(roomService.findRoomsByBuyerId(buyerId));
    }

}
