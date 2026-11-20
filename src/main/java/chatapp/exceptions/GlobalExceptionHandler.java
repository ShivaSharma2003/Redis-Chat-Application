package chatapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<?> handleRoomNotFound(
            RoomNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<?> handleDuplicateRoom(
            RoomAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(UserNotParticipantException.class)
    public ResponseEntity<?> handleUnauthorizedParticipant(
            UserNotParticipantException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(ParticipantAlreadyExistsException.class)
    public ResponseEntity<?> handleDuplicateParticipant(
            ParticipantAlreadyExistsException ex
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "status", "error",
                        "message", "Invalid request data"
                ));
    }
}
