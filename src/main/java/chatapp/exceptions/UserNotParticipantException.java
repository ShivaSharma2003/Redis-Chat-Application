package chatapp.exceptions;

public class UserNotParticipantException extends RuntimeException{
    public UserNotParticipantException(String message){
        super(message);
    }
}
