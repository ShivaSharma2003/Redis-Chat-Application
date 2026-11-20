package chatapp.dto;

import jakarta.validation.constraints.NotBlank;

public record JoinRoomRequest(

        @NotBlank(message = "Participant is required")
        String participant

) {}
