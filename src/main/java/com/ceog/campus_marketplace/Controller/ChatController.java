package com.ceog.campus_marketplace.Controller;

import com.ceog.campus_marketplace.Model.Messages;
import com.ceog.campus_marketplace.Model.Rooms;

import com.ceog.campus_marketplace.Paylode.MessageRequest;
import com.ceog.campus_marketplace.Repository.RoomsRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
// Need this import

@Controller
@CrossOrigin("*")
public class ChatController {
    private RoomsRepository roomsRepository;

    public ChatController(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Messages sendMessage(
            @DestinationVariable String roomId,
            @Payload MessageRequest request
    ){
        Rooms room=roomsRepository.findByRoomId(roomId);

        Messages message=new Messages();
        message.setChat(request.getContent());
        message.setSender(request.getSender());
        message.setTime(LocalDateTime.now());

        if(room!=null){
            message.setRoom(room);
            room.getMessages().add(message);
            roomsRepository.save(room);
        }else{
            throw new RuntimeException("room not founded");
        }
        return message;
    }
}
