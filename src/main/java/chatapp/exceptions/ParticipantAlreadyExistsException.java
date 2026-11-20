package chatapp.exceptions;

public class ParticipantAlreadyExistsException extends RuntimeException{
    public ParticipantAlreadyExistsException(String message) {
        super(message);
    }
}
