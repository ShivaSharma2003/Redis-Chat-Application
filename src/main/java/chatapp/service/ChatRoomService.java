package chatapp.service;

import chatapp.dto.ApiResponse;
import chatapp.dto.RoomParticipant;
import chatapp.exceptions.ParticipantAlreadyExistsException;
import chatapp.exceptions.RoomAlreadyExistsException;
import chatapp.exceptions.RoomNotFoundException;
import chatapp.repository.RedisChatRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RedisChatRepository repository;

    public ApiResponse createRoom(String roomName) {

        String roomId = roomName.trim().toLowerCase();

        // Check duplicate room
        if (repository.roomExists(roomId)) {
            throw new RoomAlreadyExistsException(
                    "Chat room already exists: " + roomId
            );
        }

        // Store metadata
        Map<String, String> metadata = Map.of(
                "roomId", roomId,
                "roomName", roomName.trim(),
                "createdAt", Instant.now().toString()
        );

        repository.createRoom(metadata);

        return new ApiResponse(
                "Chat room '" + roomId + "' created successfully.",
                "success",
                roomId
        );
    }

    public ApiResponse joinRoom(String roomId, String participant) {

        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException(
                    "Chat room does not exist: " + roomId
            );
        }

        if(repository.isParticipant(roomId, participant))
        {
            throw new ParticipantAlreadyExistsException(
                    "Participant already exists in room " + roomId
            );
        }

        repository.addParticipant(roomId, participant);

        return new ApiResponse(
                "User '" + participant +
                        "' joined chat room '" + roomId + "'.",
                "success",
                roomId
        );
    }

    public ApiResponse deleteRoom(String roomId) {

        // Step 1: Check whether room exists
        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException(
                    "Chat room not found: " + roomId
            );
        }

        // Step 2: Delete all associated Redis keys
        repository.deleteRoom(roomId);

        // Step 3: Return success response
        return new ApiResponse(
                "Chat room '" + roomId +
                        "' deleted successfully.",
                "success",
                roomId
        );
    }

    public List<RoomParticipant> getParticipants(String roomId) {

        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException(
                    "Chat room does not exist: " + roomId
            );
        }

        Set<String> participants =
                repository.getParticipants(roomId);

        return participants.stream()
                .map(RoomParticipant::new)
                .toList();
    }
}
