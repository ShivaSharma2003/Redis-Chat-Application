package chatapp.controller;

import chatapp.dto.*;
import chatapp.service.ChatRoomService;
import chatapp.service.MessageService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatapp")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    // CREATE ROOM
    @PostMapping("/chatrooms")
    public ApiResponse createRoom(
            @Valid @RequestBody CreateRoomRequest request
    ) {

        return chatRoomService.createRoom(
                request.roomName()
        );
    }

    // JOIN ROOM
    @PostMapping("/chatrooms/{roomId}/join")
    public ApiResponse joinRoom(
            @PathVariable String roomId,
            @Valid @RequestBody JoinRoomRequest request
    ) {

        return chatRoomService.joinRoom(
                roomId,
                request.participant()
        );
    }

    // SEND MESSAGE
    @PostMapping("/chatrooms/{roomId}/messages")
    public ApiResponse sendMessage(
            @PathVariable String roomId,
            @Valid @RequestBody SendMessageRequest request
    ) {

        messageService.sendMessage(
                roomId,
                request.participant(),
                request.message()
        );

        return new ApiResponse(
                "Message sent successfully.",
                "success",
                roomId
        );
    }

    // RETRIEVE HISTORY
    @GetMapping("/chatrooms/{roomId}/messages")
    public List<ChatMessage> getMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "10") int limit
    ) {

        return messageService.getMessages(roomId, limit);
    }

    @GetMapping("/chatrooms/{roomId}/participants")
    public List<RoomParticipant> getParticipants(
            @PathVariable String roomId
    ){
        return chatRoomService.getParticipants(roomId);
    }

    @DeleteMapping("/chatrooms/{roomId}")
    public ApiResponse deleteRoom(
            @PathVariable String roomId
    ) {

        return chatRoomService.deleteRoom(roomId);
    }
}
