package cordova_evontech_tokbox;

import android.content.Context;
import android.util.Log;

import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;

/**
 * Created by amitrai on 7/9/17.
 * User for :-
 */

public class ConnectionManager extends Session{

    private final OpenTokListener mSessionListener;
    public static boolean SESSION_CONNECTED = false;
    public static boolean CALL_STARTED= false;
    private boolean mCaller = false;
    private Session mSession = null;
    public boolean isConnected = false;


    private final String TAG = getClass().getSimpleName();


    public ConnectionManager(Context context, String apiKey, String sessonId, OpenTokListener listeners) {
        super(context, apiKey, sessonId);
        this.mSessionListener = listeners;
        CALL_STARTED = false;
    }



    // callbacks
    @Override
    protected void onConnected() {
//        mPreview = VideoPlugin.layout_publisher;
        isConnected = true;
        try {
            SESSION_CONNECTED = true;

        }catch (Exception e){
            e.printStackTrace();
            mSessionListener.onError(Appconstants.ERROR_ON_CONNECT);
        }

//        }

//        presentText("Welcome to OpenTok Chat.");
    }




    @Override
    protected void onStreamReceived(Stream stream) {

        try {
            CALL_STARTED = true;
        }catch (Exception e){
            e.printStackTrace();
            mSessionListener.onError(Appconstants.ERROR_ON_SESSION_CONNECT);
        }


    }

    @Override
    protected void onStreamDropped(Stream stream) {

        try {
        }catch (Exception e){
            e.printStackTrace();
            mSessionListener.onError(Appconstants.ERROR_STREAM_DROPED);
        }

    }

    @Override
    protected void onSignalReceived(String type, String data,
                                    Connection connection) {
        Log.e(TAG , "signal received");
        mSessionListener.onSignalMessageReceived(type, data, connection);
    }


    @Override
    protected void onStreamHasVideoChanged(Stream stream, int hasVideo) {
        super.onStreamHasVideoChanged(stream, hasVideo);
        try {

            Log.e(TAG, ""+hasVideo);
        }catch (Exception e){
            e.printStackTrace();
            mSessionListener.onError(Appconstants.ERROR_OCCURED);
        }

    }



    @Override
    protected void onConnectionDestroyed(Connection connection) {
        super.onConnectionDestroyed(connection);
        mSessionListener.onConnecionDestroyed(connection);
    }

    @Override
    protected void onConnectionCreated(Connection connection) {
        super.onConnectionCreated(connection);
        if(!mCaller)
            mSessionListener.onConnecitonCreated(connection);

        Log.e(TAG, "connectin id is "+connection.getConnectionId());
        Log.e(TAG, "connectin data is "+connection.getData());
        Log.e(TAG, "connectin time is "+connection.getCreationTime().getTime());
    }

    @Override
    protected void onArchiveStarted(String id, String name) {
        super.onArchiveStarted(id, name);
    }

    @Override
    protected void onArchiveStopped(String id) {
        super.onArchiveStopped(id);
    }

    @Override
    protected void onDisconnected() {
        super.onDisconnected();
        isConnected = false;
    }

    @Override
    protected void onError(OpentokError error) {
        super.onError(error);
        mSessionListener.onError(""+error.getMessage());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onReconnected() {
        super.onReconnected();
    }

    @Override
    protected void onReconnecting() {
        super.onReconnecting();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
