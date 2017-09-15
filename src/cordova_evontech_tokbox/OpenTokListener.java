package cordova_evontech_tokbox;

import com.opentok.android.Connection;
import com.opentok.android.Session;

/**
 * Created by amitrai on 7/9/17.
 * User for :- conveing messages to the opentok callbacks
 */

public interface OpenTokListener {
    void onError(String error_message);
    void onSuccess(String message);
    void onMessageReceived(String message_type, String message);
    void onSessionConnected(Session session);
    void onConnecionDestroyed();
    void onConnecitonCreated(Connection connection);
    void onSignalMessageReceived(String type, String data,
                     Connection connection);
}
