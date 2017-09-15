package cordova_evontech_tokbox;

import com.opentok.android.OpentokError;
import com.opentok.android.Stream;

/**
 * Created by amit rai on 3/7/2016
 */
public interface SessionListeners {
    void onStreamDrop(Stream stream);
    void onVideoViewChange(boolean hasBothVideo);
    void onCallConnected();

    void onCallDisconnected();
    void onCallRejected();
    void onCallEnded();
    void onReciverInitialized();
    void onCallerInitialized();
    void onReceiverInitialized();
    void onCallStarted();
    void onCallEndBeforeConnect();
    void onCallEndByReceiver();
    void onError(OpentokError error);
    void onPluginError(String error_message);
    void videoReceived();
    void onSessionConnected();



}
