package chatapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(

        @NotBlank(message = "Participant is required")
        String participant,

        @NotBlank(message = "Message cannot be empty")
        @Size(max = 2000)
        String message

) {}
