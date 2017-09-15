package cordova_evontech_tokbox;

import com.opentok.android.Connection;

/**
 * Created by amitrai on 14/9/17.
 * User for :-
 */

public class OpentokConnection extends Connection {
    public OpentokConnection(String connectionId, String data, long creationTime) {
        super(connectionId, creationTime, data);
    }
}
