package chatapp.dto;

public record ApiResponse(

        String message,
        String status,
        String roomId

) {}
