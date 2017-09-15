package cordova_evontech_tokbox;

/**
 * Created by amitrai on 7/9/17.
 * User for :-
 */

public class SignalMessage {

    private String message;
    private String message_type;

    public SignalMessage(String message_type, String message){
        this.message = message;
        this.message_type = message_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
}
