package cordova_evontech_tokbox;

/**
 * Created by amitrai on 7/9/17.
 * User for :-
 */

public class Appconstants {

    public static final String API_KEY = "45953692";
    public static final String SESSION_ID = "2_MX40NTk1MzY5Mn5-MTUwNTQ1MjU0NDU5Mn5Qbkh6WEo0bGl6RTdDclppOGtxMmJYeXR-fg";
    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NTk1MzY5MiZzaWc9MmVhZWU1NmFmMTczMTM3MzU4ZTEyY2FiMmE3NDJjNTA3MDA5ZjgyZTpzZXNzaW9uX2lkPTJfTVg0ME5UazFNelk1TW41LU1UVXdOVFExTWpVME5EVTVNbjVRYmtoNldFbzBiR2w2UlRkRGNscHBPR3R4TW1KWWVYUi1mZyZjcmVhdGVfdGltZT0xNTA1NDUyNjc0Jm5vbmNlPTAuNjM2ODMwNzkwMjA5MTcwMSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTA1NTM5MDcyJmNvbm5lY3Rpb25fZGF0YT1hbWl0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
//    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NTk1MzY5MiZzaWc9OWZlODZjOGQwNTU5OWUyMWFlMjBhYjhmYmYxZWY0MjAwZTAwMTA2ZDpzZXNzaW9uX2lkPTJfTVg0ME5UazFNelk1TW41LU1UVXdOVFExTWpVME5EVTVNbjVRYmtoNldFbzBiR2w2UlRkRGNscHBPR3R4TW1KWWVYUi1mZyZjcmVhdGVfdGltZT0xNTA1NDUyNjk5Jm5vbmNlPTAuNTcxMTM5OTI2OTM2MTU1OSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTA1NTM5MDk4JmNvbm5lY3Rpb25fZGF0YT1hbWl0MiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";

    public static final String ACTION_INIT_CALL = "initate_call";
    public static final String MESSAGE_TYPE_CHAT =  "sendTextMessage";


    // error messages
    public static final String API_KEY_ERROR =  "api key is not valid";
    public static final String SESSION_ID_ERROR =  "session id is not valid";
    public static final String TOKEN_ERROR =  "token is not valid";
    public static final String UNKNOWN_ERROR =  "un-know error";
    public static final String MESSAGE_ERROR =  "message receive error";



    public static final String INIT_COMPLETE = "Initialization completed !!";
    public static final String CALL_STARTED = "CallStarted";
    public static final String DISCONNECT_SUCCESS = "Successfully disconnected !!";
    public static final String CALL_END = "CallEnded";
    public static final String CALL_END_BEFORE_CONNECT = "callEndedByUser";

    public static final String INITIALIZATION_COMPLETE = "Initialization completed !!";
    public static final String RECEIVER_INITIALIZED = "ReceiverInitializationCompleted";
    public static final String CONNECTION_CREATED = "connectionCreated";
    public static final String CALL_ENDED_BY_RECEIVER = "callEndedByUser";

    public static final String TEN_DOLLARS = "10";
    public static final String TWENTY_DOLLARS = "20";
    public static final String FOURTY_DOLLARS = "40";
    public static final String SIXTY_DOLLARS = "60";

    public static final String USER_TYPE_PRO = "Pro";



    /**
     * speed examples server host name.
     */
    public final static String SPEED_TEST_SERVER_HOST = "speedtestdlhi1.rcom.co.in";//"2.testdebit.info";

    /**
     * spedd examples server uri.
     */
    public final static String SPEED_TEST_SERVER_URI_DL = "/speedtest/random350x350.jpg";//"/fichiers/10Mo.dat";

    /**
     * speed examples server port.
     */
    public final static int SPEED_TEST_SERVER_PORT = 80;

    // conversion constant
    public static final int VALUE_PER_SECONDS = 1024;

    public static final float MIN = 250;
    public static final float MAX = 400;

    public static final int LOW = 1;
    public static final int MEDIUM = 2;
    public static final int HIGH = 3;



    public static String ERROR_OCCURED = "an error occured";
    public static String ERROR_ON_CONNECT = "error while connecting";
    public static String ERROR_ON_SESSION_CONNECT = "error on session connect";
    public static String ERROR_STREAM_DROPED = "stream dropped";
}
