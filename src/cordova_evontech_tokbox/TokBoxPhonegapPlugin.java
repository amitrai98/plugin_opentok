package cordova_evontech_tokbox;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.R;
import com.google.gson.Gson;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.data;
import static android.R.attr.type;


/**
 * This class echoes a string called from JavaScript.
 */
public class TokBoxPhonegapPlugin extends CordovaPlugin implements SessionListeners , View.OnClickListener,
        Session.SignalListener{

    public static final String ACTION_INIT_CALL = "initializeVideoCall";//"initializeVideoCalling";
    public static final String ACTION_ENDCALL = "endCalling";
    public static final String ACTION_SENDMESSAGE= "sendTextMessage";
    public static final String ACTION_CONNECT_TO_SESSION= "connectToSession";
    private static final String TAG = CordovaPlugin.class.getSimpleName();
    private static MySession mSession;
    private ImageView mVideoCallBtn, mMicBtn, mDisconnectBtn, mSwipeBtn;
    private View mCallView, mNoneView;
    private ViewGroup mViewGroup;
    private LinearLayout mParentProgressDialog;
    private Chrono mTimerTxt;
    private String mCallPerMinute, mUserBalance = "0", mProfileImageUrl;
    private RelativeLayout mCallingViewParent;
    private boolean isCallingViewVisible = true;
    private Handler handler = new Handler();
    private Runnable callRunnable;
    private CallbackContext mCallBackContext;
    private ImageView mProfilePicConnecting = null;
    private ImageView mImageNonView = null;
    private boolean CALL_DISCONNECT = false;
//    private com.listeners.SessionListeners sessionListeners = null;

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    private String MISSED_CALL = null;
    private boolean prev_command =false;
    private boolean call_initialized = false;
    private boolean mCaller = false;
    private boolean mDisconnect = false;
    private boolean mMissedCall = false;

    private CallBean callBean = null;

    // new view changes
    //    private ImageView img_add_credit = null;
    private ProgressDialog dialogWait = null;
    private TextView tv_username = null;
    private LinearLayout layout_header_addcredit = null;

    private RelativeLayout layout_enable_disable_video = null;
    private TextView txt_connecting = null;
    private ProgressBar progressbar = null;

//    private RelativeLayout layout_send_tip = null;

    // tip dialog
    private LinearLayout layout_progress_tip = null;
    private RelativeLayout layout_others = null;
    private Button btn_lowbal = null;

    //add credit
    private LinearLayout layout_progress = null;
    private LinearLayout layout_credit_btns = null;

    public static RelativeLayout layout_publisher = null;



    private JSONObject jsonResponse =null;
    private ConnectionManager manager = null;
    public Connection connection_;


    /**
     * connects to the session
     * @param args
     * @return
     */
    private boolean connectToSession(final JSONArray args) {
        try {
            Gson gson = new Gson();
//            callBean = gson.fromJson(args.get(0).toString(), CallBean.class);

            final MessageBean messageBean = gson.fromJson(args.get(0).toString(), MessageBean.class);

            messageBean.setApiKey(Appconstants.API_KEY);
            messageBean.setToken(Appconstants.TOKEN);
            messageBean.setSessionId(Appconstants.SESSION_ID);


            if (manager != null)
                manager.disconnect();

            manager = new ConnectionManager(cordova.getActivity(), messageBean.getApiKey(),
                    messageBean.getSessionId(), new OpenTokListener() {


                @Override
                public void onError(String error_message) {
                    Log.e(TAG , "on error");

                }

                @Override
                public void onSuccess(String message) {
                    Log.e(TAG , "on error");
                }


                @Override
                public void onSessionConnected(Session session) {
                    try {
                        session.setSignalListener(TokBoxPhonegapPlugin.this);
                        Log.e(TAG, "connected to session");
                        if (mCallBackContext != null){

                            JSONObject jsonData = new JSONObject();
                            jsonData.put("sessionId", session.getSessionId());
                            sendSuccess(jsonData, "SessionConnected");
                        }
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }
                }

                @Override
                public void onConnecionDestroyed(Connection connection) {
                    if (mCallBackContext != null){

                        try {
                            JSONObject jsonData = new JSONObject();
                            jsonData.put("ConnectionId", connection.getConnectionId());
                            jsonData.put("CreationTime", connection.getCreationTime());
                            jsonData.put("Data", connection.getData());
                            sendSuccess(jsonData, Appconstants.ConnectionDestroyed);
                        }catch (Exception exp){
                            exp.printStackTrace();
                        }

                    }
                }

                @Override
                public void onConnecitonCreated(Connection connection) {
//                    TokBoxPhonegapPlugin.this.connection = connection;

                    try {
                        JSONObject jsonData = new JSONObject();
                        jsonData.put("ConnectionId", connection.getConnectionId());
                        jsonData.put("CreationTime", connection.getCreationTime());
                        jsonData.put("Data", connection.getData());
                        sendSuccess(jsonData, Appconstants.ConnectionCreated);
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }
                }

                @Override
                public void onSignalMessageReceived(String type, String data, Connection connection) {
                    Log.e(TAG, "type is "+type +" connection data "+data+" connction id"+connection.getConnectionId());

                    try {
                        JSONObject responseObject = new JSONObject();
                        JSONObject messageJson = new JSONObject();
                        JSONObject jsonConnection = new JSONObject();

                        jsonConnection.put("ConnectionId", connection.getConnectionId());
                        jsonConnection.put("CreationTime", connection.getCreationTime());
                        jsonConnection.put("Data", connection.getData());

                        messageJson.put("ConnectionData", jsonConnection);
                        messageJson.put("MessageBody", data);
                        messageJson.put("MessageType", type );
                        responseObject.put("MessageData", messageJson);
                        sendSuccess(responseObject, Appconstants.SignalReceived);
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }

                }
            });

            manager.connect(messageBean.getToken());





        }catch (Exception exp){
            exp.printStackTrace();
            return false;
        }

        return true;
    }


    //  change color of the tip bottom and hide it put check for low balance and corner rounded (done)
    //  increase the text size of credit (done)
    //  add checks for pro and normal user for showing different views (done)
    //  add check if user is able the call in not then freeze on the add credit screen. (done)
    //  add check for caller init and receiver init on start of call (done)

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
    }

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action != null && !action.isEmpty()){

            if(action.equalsIgnoreCase(ACTION_INIT_CALL)){
                //checks current connection bandwidth
                mCallBackContext = callbackContext;
                initVideoCall(action, args, callbackContext);
            }

            else if(action.equalsIgnoreCase(ACTION_ENDCALL)){
                String object = args.getString(0);
                mMissedCall = true;
                mCallBackContext = callbackContext;
                endCall(object);
            }

            else if(action.equalsIgnoreCase(ACTION_SENDMESSAGE)){
                mCallBackContext = callbackContext;
                return  sendMessage(args);
            }

            else if(action.equalsIgnoreCase(ACTION_CONNECT_TO_SESSION)){
                mCallBackContext = callbackContext;
                return  connectToSession(args);
            }
        }
        return false;
    }

    /**
     * initiates video calling
     * @param callBean all initial json data for calling
     */
    private void initCall(CallBean callBean, int CALL_QUALITY) {
        try {


            mDisconnect = false;
            call_initialized = true;
            CALL_DISCONNECT = false;
            String apiKey = callBean.getApiKey();//.getString("apiKey");
            String sessonId = callBean.getSessionId();//object.getString("sessonId");
            String sessonToken = callBean.getToken();//object.getString("sessonToken");
            mCallPerMinute = callBean.getCallPerMinute();//object.getString("callPerMinute");
            mProfileImageUrl = callBean.getProfileImage();//object.getString("profileImageUrl");


            if(callBean != null){
                mCaller = false;
                JSONObject json = getJson(Constants.INIT_COMPLETE, SUCCESS);
//                mCallBackContext.successMessage(json);
                mSession = new MySession(cordova.getActivity(), this, apiKey, sessonId, false, CALL_QUALITY);
            }
            else{
                mCaller = true;
                JSONObject json = getJson(Constants.INITIALIZATION_COMPLETE, SUCCESS);
//                mCallBackContext.successMessage(json);
                mSession = new MySession(cordova.getActivity(), this, apiKey, sessonId, true, CALL_QUALITY);
            }

            addView();
            mSession.connect(sessonToken);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * adds the video calling view to the parrent layout.
     */
    private void addView() {
        mViewGroup = (ViewGroup) webView.getView();

        LayoutInflater inflator = LayoutInflater.from(cordova.getActivity());
        mCallView = inflator.inflate(R.layout.room, null);

        tv_username = (TextView) mCallView.findViewById(R.id.tv_username);
        layout_header_addcredit = (LinearLayout) mCallView.findViewById(R.id.layout_header_addcredit);

        layout_enable_disable_video = (RelativeLayout) mCallView.findViewById(R.id.layout_enable_disable_video);

        layout_enable_disable_video.setOnClickListener(this);
        txt_connecting = (TextView) mCallView.findViewById(R.id.txt_connecting);
//        layout_send_tip = (RelativeLayout) mCallView.findViewById(R.id.layout_send_tip);

//        if (callBean.getUserType().equalsIgnoreCase(Constants.USER_TYPE_PRO)){
        layout_header_addcredit.setVisibility(View.INVISIBLE);
//        }


        setMargins(mCallView, 0, 500,0,0);

//        if(callBean.getIsAbleToCall().equalsIgnoreCase("true")){
        layout_publisher = (RelativeLayout) mCallView.findViewById(R.id.layout_publisher);  // User View
        layout_publisher.setVisibility(View.VISIBLE);
        mSession.setmPublisherViewContainer(layout_publisher);

        RelativeLayout layout_subscriber = (RelativeLayout) mCallView.findViewById(R.id.layout_subscriber);  // Subscriber View
        mSession.setmSubscribersViewContainer(layout_subscriber);
//        }


        // Progress bar Views
        mParentProgressDialog = (LinearLayout) mCallView.findViewById(R.id.ll_parent_connecting);
        mProfilePicConnecting = (ImageView) mCallView.findViewById(R.id.iv_connecting_img);
//        TextView price = (TextView) mCallView.findViewById(R.id.tv_dialog_price);
//        if(callBean != null && callBean.getUserType().equalsIgnoreCase(Constants.USER_TYPE_PRO))
//            price.setText(Html.fromHtml("<font color=\"#c5c5c5\">" + "User will be charged " + "</font>" + "<font color=\"#F0AF32\">" + callBean.getCallPerMinute()+ "$"+"</font>"+ "<font color=\"#c5c5c5\">" + " per minute <br> once video call connects"+"</font>"));//setText(cordova.getActivity().getString(R.string.call_connect_price, callBean.getCallPerMinute()));//("Once connected this video chat \n will be billed at " + mCallPerMinute + " per min.");//setText(cordova.getActivity().getString(R.string.pro_call_coast, callBean.getCallPerMinute()));//("Once connected this video chat \n will be billed at " + mCallPerMinute + " per min.");
//        else
//            price.setText(cordova.getActivity().getString(R.string.call_connect_price, callBean.getCallPerMinute()));//("Once connected this video chat \n will be billed at " + mCallPerMinute + " per min.");


        //        creator.into(mProfilePicConnecting);
        progressbar = (ProgressBar) mCallView.findViewById(R.id.pb_connecting);
        progressbar.getIndeterminateDrawable().setColorFilter(ContextCompat.
                        getColor(cordova.getActivity(), android.R.color.holo_green_dark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        mNoneView = mCallView.findViewById(R.id.non_view);
        mImageNonView = (ImageView) mCallView.findViewById(R.id.iv_no_view_img);
//        creator.into(mImageNonView);


//        mPricePopUp = (CardView) mCallView.findViewById(R.id.cv_connecting_price_dialog);

//        if (mCallPerMinute.equalsIgnoreCase("0")) {
//            mPricePopUp.setVisibility(View.GONE);
//        }

        /*Calling Views*/
        mCallingViewParent = (RelativeLayout) mCallView.findViewById(R.id.rl_calling_view);
        mVideoCallBtn = (ImageView) mCallView.findViewById(R.id.iv_video_call);
        mVideoCallBtn.setEnabled(false);
        mMicBtn = (ImageView) mCallView.findViewById(R.id.iv_audio_call);
        mMicBtn.setEnabled(false);
        mDisconnectBtn = (ImageView) mCallView.findViewById(R.id.iv_end_call);
        mSwipeBtn = (ImageView) mCallView.findViewById(R.id.iv_swipe_camera);
        mSwipeBtn.setEnabled(false);
        mTimerTxt = (Chrono) mCallView.findViewById(R.id.cm_timer);
        mVideoCallBtn.setOnClickListener(this);
        mMicBtn.setOnClickListener(this);
        mDisconnectBtn.setOnClickListener(this);
        mSwipeBtn.setOnClickListener(this);
        layout_header_addcredit.setOnClickListener(this);


        if(callBean != null && callBean.getUserName()
                != null && !callBean.getUserName().isEmpty())
            tv_username.setText(callBean.getUserName());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mViewGroup.addView(mCallView);
//                RequestCreator creator = Picasso.with(cordova.getActivity()).load(mProfileImageUrl);
//                creator.into(mProfilePicConnecting);
//                creator.into(mImageNonView);
                mTimerTxt.setActivity(cordova.getActivity());

                mTimerTxt.setOnChronometerTickListener(new Chrono.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chrono chronometer) {
//                        mTimerTxt.setText(chronometer.getText());
                    }
                });
            }
        };
        cordova.getActivity().runOnUiThread(runnable);


    }

    @Override
    public void onStreamDrop(Stream stream) {

        disconnectCall();
    }

    @Override
    public void onVideoViewChange(boolean hasVideo) {
//        mCallBackContext.success("Video View Change");
        if (hasVideo) {
            mNoneView.setVisibility(View.INVISIBLE);
        } else {
            mNoneView.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onPublisherCreate() {
//        mCallBackContext.success("init complete ");
//    }

    @Override
    public void onCallConnected() {
        JSONObject json = getJson(Constants.CONNECTION_CREATED, SUCCESS);
//        mCallBackContext.successMessage(json);
        pushResultToPlugin(PluginResult.Status.OK, json);


        if (mNoneView != null && mNoneView.getVisibility() == View.VISIBLE)
            mNoneView.setVisibility(View.GONE);

        if(mCallPerMinute != null ){

//            layout_tip_send_receive.setVisibility(View.VISIBLE);
//            layout_tip.setVisibility(View.VISIBLE);
//            layout_low_credit.setVisibility(View.VISIBLE);

            JSONObject json_callstarted = getJson(Constants.CALL_STARTED, SUCCESS);
//            mCallBackContext.successMessage(json_callstarted);
            pushResultToPlugin(PluginResult.Status.OK, json_callstarted);

//            mParentProgressDialog.setVisibility(View.GONE);
            mVideoCallBtn.setEnabled(true);
            mMicBtn.setEnabled(true);
            mDisconnectBtn.setEnabled(true);
            mSwipeBtn.setEnabled(true);
            mSwipeBtn.setVisibility(View.VISIBLE);
            mCallView.setEnabled(true);
            mCallView.setOnClickListener(this);


            mTimerTxt.setBase(SystemClock.elapsedRealtime());
            mTimerTxt.start();

            visibleCallingViews();
            callThread();

            //aaabb/123456 susie/123456
        }
    }

    /**
     * animates the visibility of the views to visible
     *
     */
    private void visibleCallingViews() {
//        mCallingViewParent.setVisibility(View.VISIBLE);
        SlideToAbove();
        SlideToLeft();
//        addCreditSlideIn();
        mSwipeBtn.setVisibility(View.VISIBLE);
        isCallingViewVisible = true;
        callThread();
    }

    /**
     * animates the visibility of the views to invisible
     */
    private void invisibleCallingViews() {
        SlideToDown();
        SlideToRight();
//        addCreditSlideOut();

        isCallingViewVisible = false;
    }


    /**
     * thread to change the visibility of the controls on ui thread
     * after some interval.
     */
    private void callThread() {
        callRunnable = new Runnable() {
            @Override
            public void run() {

                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invisibleCallingViews();
                    }
                });

            }
        };
        handler.postDelayed(callRunnable, 3000);
    }

    @Override
    public void onCallDisconnected() {
        disconnectCall();
        JSONObject json;
        if(mMissedCall)
            return;

        if(mCaller && !MySession.CALL_STARTED)
            json = getJson(Constants.CALL_ENDED_BY_RECEIVER, SUCCESS);
        else
            json = getJson(Constants.CALL_END, SUCCESS);

//        mCallBackContext.successMessage(json);
        pushResultToPlugin(PluginResult.Status.OK, json);
    }

    @Override
    public void onCallRejected() {
        JSONObject json = getJson(Constants.CALL_ENDED_BY_RECEIVER, SUCCESS);
//        mCallBackContext.successMessage(json);
        pushResultToPlugin(PluginResult.Status.OK, json);
    }

    @Override
    public void onCallStarted() {

        JSONObject json = getJson(Constants.CALL_STARTED, SUCCESS);
//        mCallBackContext.successMessage(json);
        pushResultToPlugin(PluginResult.Status.OK, json);

        if(mCallPerMinute != null && !mCallPerMinute.equals("0")){

//            mCallBackContext.success(Constants.CALL_STARTED);
            mParentProgressDialog.setVisibility(View.GONE);
//            txt_connecting.setText("Coneected");
            progressbar.setVisibility(View.GONE);
            mVideoCallBtn.setEnabled(true);
            mMicBtn.setEnabled(true);
            mDisconnectBtn.setEnabled(true);
            mSwipeBtn.setEnabled(true);
            mSwipeBtn.setVisibility(View.VISIBLE);
            mCallView.setEnabled(true);
            mCallView.setOnClickListener(this);

            mTimerTxt.setBase(SystemClock.elapsedRealtime());
            mTimerTxt.start();

            visibleCallingViews();
            callThread();
        }
    }

    @Override
    public void onCallEndBeforeConnect() {
        JSONObject json;

        if(mMissedCall)
            return;

        if(mDisconnect)
            json = getJson(Constants.CALL_ENDED_BY_RECEIVER, SUCCESS);
        else
            json = getJson(Constants.CALL_END, SUCCESS);

//        mCallBackContext.successMessage(json);
        pushResultToPlugin(PluginResult.Status.OK, json);
    }

    @Override
    public void onCallEndByReceiver() {
        JSONObject json = getJson(Constants.CALL_END, SUCCESS);
//        mCallBackContext.successMessage(json);

        pushResultToPlugin(PluginResult.Status.OK, json);
    }

    @Override
    public void onError(OpentokError error) {
        if(error != null){
            JSONObject json = getJson(error.toString(), ERROR);
            mCallBackContext.error(json);
        }
    }

    @Override
    public void onCallEnded() {
        JSONObject json = getJson(Constants.CALL_END, SUCCESS);
//        mCallBackContext.successMessage(json);

        pushResultToPlugin(PluginResult.Status.OK, json);
    }

    @Override
    public void onReciverInitialized() {
        if(callBean.getIsReceiverInit().equalsIgnoreCase("false")){
            JSONObject json = getJson(Constants.INITIALIZATION_COMPLETE, SUCCESS);
//            mCallBackContext.successMessage(json);

            pushResultToPlugin(PluginResult.Status.OK, json);
        }else {
            JSONObject json = getJson(Constants.RECEIVER_INITIALIZED, SUCCESS);
//            mCallBackContext.successMessage(json);
            pushResultToPlugin(PluginResult.Status.OK, json);
        }

    }

    @Override
    public void onCallerInitialized() {
        if(callBean.getIsReceiverInit().equalsIgnoreCase("false")){
            JSONObject json = getJson(Constants.INITIALIZATION_COMPLETE, SUCCESS);
//            mCallBackContext.successMessage(json);
            pushResultToPlugin(PluginResult.Status.OK, json);
        }else {
            JSONObject json = getJson(Constants.RECEIVER_INITIALIZED, SUCCESS);
//            mCallBackContext.successMessage(json);
            pushResultToPlugin(PluginResult.Status.OK, json);
        }
    }

    @Override
    public void onReceiverInitialized() {
        JSONObject json = getJson(Constants.RECEIVER_INITIALIZED, SUCCESS);
//        mCallBackContext.successMessage(json);
        pushResultToPlugin(PluginResult.Status.OK, json);
    }

    /**
     * if anything goes wrong
     * @param error reason for the error
     */
    @Override
    public void onPluginError(String error){
        if(error != null){
            JSONObject json = getJson(error.toString(), ERROR);
            mCallBackContext.error(json);
        }
    }

    @Override
    public void videoReceived() {
        if(mParentProgressDialog != null)
            mParentProgressDialog.setVisibility(View.GONE);
    }

    @Override
    public void onSessionConnected(Session session) {
        session.setSignalListener(TokBoxPhonegapPlugin.this);
        try {
            session.setSignalListener(TokBoxPhonegapPlugin.this);
            Log.e(TAG, "connected to session");
            if (mCallBackContext != null){

                JSONObject jsonData = new JSONObject();
                jsonData.put("sessionId", session.getSessionId());
                sendSuccess(jsonData, "SessionConnected");
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }

    }

    @Override
    public void onSignalMessageReceived(String type, String data, Connection connection) {
        Log.e(TAG, "type is "+type +" connection data "+data+" connction id"+connection.getConnectionId());

        try {
            JSONObject responseObject = new JSONObject();
            JSONObject messageJson = new JSONObject();
            JSONObject jsonConnection = new JSONObject();

            jsonConnection.put("ConnectionId", connection.getConnectionId());
            jsonConnection.put("CreationTime", connection.getCreationTime());
            jsonConnection.put("Data", connection.getData());

            messageJson.put("ConnectionData", jsonConnection);
            messageJson.put("MessageBody", data);
            messageJson.put("MessageType", type );
            responseObject.put("MessageData", messageJson);
            sendSuccess(responseObject, Appconstants.SignalReceived);
        }catch (Exception exp){
            exp.printStackTrace();
        }

    }





    /**
     * Disconnects the call
     */
    private void disconnectCall() {
        try {

            if (CALL_DISCONNECT)
                return;

            CALL_DISCONNECT = true;

            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mViewGroup.removeView(mCallView);
                }
            });

//            ((MainActivity) cordova.getActivity()).setActivityListener(null);
//            if(MySession.CALL_CONNECTED){
////            JSONObject json = getJson(Constants.CALL_END, SUCCESS);
////            mCallBackContext.successMessage(json);
//                Log.e(TAG, "call connected");
//            }
//            if (mSession.getSubscriber() != null) {
////            JSONObject json = getJson(Constants.CALL_END, SUCCESS);
////            mCallBackContext.successMessage(json);
//            }
            if(mCallPerMinute != null && mCallPerMinute.equals("0")){
                JSONObject json = getJson(Constants.CALL_ENDED_BY_RECEIVER, SUCCESS);
//                mCallBackContext.successMessage(json);
                pushResultToPlugin(PluginResult.Status.OK, json);
            }

            else {
                JSONObject json = getJson(Constants.DISCONNECT_SUCCESS, SUCCESS);
                JSONObject endcalljson = getJson(Constants.CALL_END_BEFORE_CONNECT, SUCCESS);

//                mCallBackContext.successMessage(json);
//                mCallBackContext.successMessage(endcalljson);

                pushResultToPlugin(PluginResult.Status.OK, json);
                pushResultToPlugin(PluginResult.Status.OK, endcalljson);
            }

            mSession.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_audio_call:
                muteAudio();
                break;
            case R.id.iv_video_call:
                hideCam();
                break;
            case R.id.iv_end_call:
                mDisconnect = true;
                disconnectCall();
                break;

            case R.id.iv_swipe_camera:
                mSession.swipeCamera();
                break;
            case R.id.mainlayout:
                if (isCallingViewVisible) {
                    invisibleCallingViews();
                    handler.removeCallbacks(callRunnable);
                } else {
                    visibleCallingViews();
                }
                break;

        }
    }

    /**
     * Hide/Show Video Camera of Subscriber
     */
    public void hideCam() {
        mSession.hideVideo();
        if (mSession.isCameraOn()) {
            mVideoCallBtn.setImageResource(R.drawable.camera);

        } else {
            mVideoCallBtn.setImageResource(R.drawable.camera_no);
        }
    }

    /**
     * Enable/Disable Mic.
     */
    public void muteAudio() {
        mSession.muteMic();
        if (mSession.isMicMuted()) {
            mMicBtn.setImageResource(R.drawable.mic_no);
        } else {
            mMicBtn.setImageResource(R.drawable.mic);
        }
    }


    /**
     * animation to slide the view above.
     */
    public void SlideToAbove() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                10f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mCallingViewParent.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mCallingViewParent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

        });

    }


    /**
     * animation to slide the view down.
     */
    public void SlideToDown() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);

        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mCallingViewParent.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallingViewParent.setVisibility(View.GONE);
            }

        });

    }

    /**
     * animation to slide the view left.
     */
    public void SlideToLeft() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 10.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mSwipeBtn.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mSwipeBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

        });

    }

    /**
     * slides upward
     */
//    private void slideToUp(){
//        Animation slide = null;
//        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 5.2f, Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//
//        slide.setDuration(600);
//        slide.setFillAfter(true);
//        slide.setFillEnabled(true);
//        mSwipeBtn.startAnimation(slide);
//
//        slide.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                mSwipeBtn.setVisibility(View.GONE);
////                mCallingViewParent.clearAnimation();
////
////                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
////                        mCallingViewParent.getWidth(), mCallingViewParent.getHeight());
////                lp.setMargins(0, mCallingViewParent.getWidth(), 0, 0);
////                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
////                mCallingViewParent.setLayoutParams(lp);
//
//            }
//
//        });
//    }

    /**
     * slides the view to the right
     */
    public void SlideToRight() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 5.2f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mSwipeBtn.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mSwipeBtn.setVisibility(View.INVISIBLE);
//                mCallingViewParent.clearAnimation();
//
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        mCallingViewParent.getWidth(), mCallingViewParent.getHeight());
//                lp.setMargins(0, mCallingViewParent.getWidth(), 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                mCallingViewParent.setLayoutParams(lp);

            }

        });

    }
//
//    /**
//     * slides out the credit button
//     */
//    private void addCreditSlideIn(){
//        Animation animation = new TranslateAnimation(-500, 0,0, 0);
//        animation.setDuration(600);
//        animation.setFillAfter(true);
//        animation.setFillEnabled(true);
//
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
////                if(!callBean.getUserType().equalsIgnoreCase(Constants.USER_TYPE_PRO))
////                    layout_tip.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    /**
//     * slides out the credit button
//     */
//    private void addCreditSlideOut(){
//        Animation animation = new TranslateAnimation(0, -500,0, 0);
//        animation.setDuration(600);
//        animation.setFillAfter(true);
//        animation.setFillEnabled(true);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }

//    @Override
//    public void onPauseActivity() {
//        if (mSession != null) {
//            mSession.onPause();
//        }
//    }
//
//    @Override
//    public void onResumeActivity() {
//        if (!resumeHasRun) {
//            resumeHasRun = true;
//        } else {
//            if (mSession != null) {
//                mSession.onResume();
//            }
//        }
//    }
//
//    @Override
//    public void onStoppedActivity() {
////        if (mSession != null) {
////            mSession.disconnect();
////        }
//    }

//    @Override
//    public void onDestroyActivity() {
//        if (mSession != null) {
//            mSession.disconnect();
//        }
//    }
//
//    @Override
//    public void onRequestAccessed() {
//        Log.e(TAG, "y m i commented");
//    }

    /**
     * returns json for the message
     * @param message to be sent to cordova
     * @param message_type is success or failure
     * @return returns json object for the message
     */
    private JSONObject getJson(String message, String message_type){


        try {
            if(message == null || message.equalsIgnoreCase(" ")){
                return new JSONObject("{\"data\":\" \",\"status\":\"success\"}");
            }else if(message.isEmpty()){
                return new JSONObject("{\"data\":\" \",\"status\":\"success\"}");
            }

            if(message_type.equals(SUCCESS)){
                if(message.equals("Initialization completed !!"))
                    jsonResponse = new JSONObject("{\"data\":\"Initialization completed !!\",\"status\":\"success\"}");
                else if(message.equals("Successfully disconnected !!"))
                    jsonResponse = new JSONObject("{\"data\":\"Successfully disconnected !!\",\"status\":\"success\"}");
                else
                    jsonResponse = new JSONObject("{\"data\":"+message+",\"status\":\"success\"}");

                return jsonResponse;

            }else if(message_type.equals(ERROR)){
//                String network_type = getNetworkInfo();
//                jsonResponse = new JSONObject("{\"data\":\"{\\\"networkType\\\":"+network_type+",\\\"error\\\":"+message+"}\",\"status\":\"failure\"}");
                return jsonResponse;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * sets margin to a view
     * @param v vertical
     * @param l from left
     * @param t from top
     * @param r from right
     * @param b from bottom
     */
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * this method returns the call back as it is and removes the calling view.
     */
    private void endCall(String message){
        try {

            if(!call_initialized)
                return;

            if(prev_command)
                return;

            prev_command = true;


            if (mSession != null)
                mSession.disconnect();

            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mViewGroup.removeView(mCallView);
                }
            });


            if(message != null && message.equalsIgnoreCase("missedCall")){
                JSONObject json = getJson(message, SUCCESS);
//                mCallBackContext.successMessage(json);
                pushResultToPlugin(PluginResult.Status.OK, json);
            }else if(message == null || message .equalsIgnoreCase("null")){
                JSONObject json = getJson(null, SUCCESS);
//                mCallBackContext.successMessage(json);
                pushResultToPlugin(PluginResult.Status.OK, json);
            }else {
                JSONObject json = getJson(message, SUCCESS);
//                mCallBackContext.successMessage(json);
                pushResultToPlugin(PluginResult.Status.OK, json);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * initialize video call
     */
    private boolean initVideoCall(String action, JSONArray args, CallbackContext callbackContext){
        try {
            prev_command =false;
            mMissedCall = false;

            dialogWait = new ProgressDialog(cordova.getActivity());
            dialogWait.setMessage(cordova.getActivity().getResources().getString(R.string.wait_message));

            Log.e(TAG, ""+args);

            Gson gson = new Gson();
            callBean = gson.fromJson(args.get(0).toString(), CallBean.class);
            callBean.setSessionId(Constants.SESSION_ID);
            callBean.setApiKey(Constants.API_KEY);
            callBean.setToken(Constants.TOKEN);
            Log.e(TAG, ""+callBean);
            initCall(callBean, Constants.LOW);
            if (ACTION_INIT_CALL.equals(action)) {
                mCallBackContext = callbackContext;
//                ((MainActivity) cordova.getActivity()).setActivityListener(this);

                int permissionCheck = ContextCompat.checkSelfPermission(cordova.getActivity(),
                        Manifest.permission.CAMERA);

                int permissionCheckAudio = ContextCompat.checkSelfPermission(cordova.getActivity(),
                        Manifest.permission.RECORD_AUDIO);

                int permissionCheckModifyAudio = ContextCompat.checkSelfPermission(cordova.getActivity(),
                        Manifest.permission.MODIFY_AUDIO_SETTINGS);

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getBandWidthSpeed(new SpeedTestListener() {
//                            @Override
//                            public void onDownloadComplete(final int QUALITY) {
//                                cordova.getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        initCall(callBean, QUALITY);
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onDownloadFailed() {
//                                cordova.getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        initCall(callBean, Constants.LOW);
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }).start();



                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheckAudio == PackageManager.PERMISSION_GRANTED && permissionCheckModifyAudio == PackageManager.PERMISSION_GRANTED) {
//                    cordova.getThreadPool().execute(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
                    Log.e(TAG, "call received");

                } else {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(),
                            Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(cordova.getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                100);
                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(),
                            Manifest.permission.RECORD_AUDIO)) {
                        ActivityCompat.requestPermissions(cordova.getActivity(),
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                100);
                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(cordova.getActivity(),
                            Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
                        ActivityCompat.requestPermissions(cordova.getActivity(),
                                new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                                100);
                    } else {
                        ActivityCompat.requestPermissions(cordova.getActivity(),
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS},
                                100);
                    }
                }
                return true;
            } else if(action!=null && action.equalsIgnoreCase("missedCall")){
                mMissedCall = true;
                endCall(action);
            }else if (ACTION_ENDCALL.equalsIgnoreCase(action)) {
                if(args != null){
                    String object = args.getString(0);
                    mMissedCall = true;
                    endCall(object);
                }else
                    disconnectCall();
            }else if(action == null || action.equalsIgnoreCase("null")){
                endCall(null);
            }else
                endCall(action);
            callbackContext.error("Invalid action");
            return false;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }



    private boolean sendMessage(JSONArray args){
        try {
            Gson gson = new Gson();
            callBean = gson.fromJson(args.get(0).toString(), CallBean.class);

            final MessageBean messageBean = gson.fromJson(args.get(0).toString(), MessageBean.class);

            messageBean.setApiKey(Appconstants.API_KEY);
            messageBean.setToken(Appconstants.TOKEN);
            messageBean.setSessionId(Appconstants.SESSION_ID);

            if (manager == null ){
                manager = new ConnectionManager(cordova.getActivity(), messageBean.getApiKey(), messageBean.getSessionId(), new OpenTokListener() {

                    @Override
                    public void onError(String error_message) {
                        Log.e(TAG , "on error");
                    }

                    @Override
                    public void onSuccess(String message) {
                        Log.e(TAG , "on error");
                    }


                    @Override
                    public void onSessionConnected(Session session) {

                        session.setSignalListener(TokBoxPhonegapPlugin.this);

//                        try {
//                            SignalMessage message = new SignalMessage(messageBean.getMessageData().getMessageType(),
//                                    messageBean.getMessageData().getMessageBody());
//                            if (manager != null && messageBean.getMessageData().getConnectionData() != null){
//                                MessageBean.ConnectionData data = messageBean.getMessageData().getConnectionData();
////                            long connection_time = Long.parseLong(data.getConnectionId());
//                        OpentokConnection  connection = new OpentokConnection(data.getConnectionId(), data.getData(), 0);
//                                if (connection != null)
//                                    sendMessage(message, connection);
//                                else
//                                    sendGroupMessage(message);
//                            }
//                        }catch (Exception exp){
//                            exp.printStackTrace();
//                        }


                        try {
                            Log.e(TAG, "connected to session");
                            if (mCallBackContext != null){

                                JSONObject jsonData = new JSONObject();
                                jsonData.put("sessionId", session.getSessionId());
                                sendSuccess(jsonData, "SessionConnected");
                            }
                        }catch (Exception exp){
                            exp.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnecionDestroyed(Connection connection) {
                        if (mCallBackContext != null){

                            try {
                                JSONObject jsonData = new JSONObject();
                                jsonData.put("ConnectionId", connection.getConnectionId());
                                jsonData.put("CreationTime", connection.getCreationTime());
                                jsonData.put("Data", connection.getData());
                                sendSuccess(jsonData, Appconstants.ConnectionDestroyed);
                            }catch (Exception exp){
                                exp.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onConnecitonCreated(Connection connection) {
//                        TokBoxPhonegapPlugin.this.connection = connection;

                        try {
                            JSONObject jsonData = new JSONObject();
                            jsonData.put("ConnectionId", connection.getConnectionId());
                            jsonData.put("CreationTime", connection.getCreationTime());
                            jsonData.put("Data", connection.getData());
                            sendSuccess(jsonData, Appconstants.ConnectionCreated);
                        }catch (Exception exp){
                            exp.printStackTrace();
                        }




                        try {
                            SignalMessage message = new SignalMessage(messageBean.getMessageData().getMessageType(),
                                    messageBean.getMessageData().getMessageBody());
                            if (manager != null && messageBean.getMessageData().getConnectionData() != null){
                                MessageBean.ConnectionData data = messageBean.getMessageData().getConnectionData();
//                            long connection_time = Long.parseLong(data.getConnectionId());
                                OpentokConnection  msgConnection = new OpentokConnection(data.getConnectionId(), data.getData(), 0);
                                if (connection != null)
                                    sendMessage(message, msgConnection);
                                else
                                    sendGroupMessage(message);
                            }
                        }catch (Exception exp){
                            exp.printStackTrace();
                        }

                    }

                    @Override
                    public void onSignalMessageReceived(String type, String data, Connection connection) {
                        Log.e(TAG, "type is "+type +" connection data "+data+" connction id"+connection.getConnectionId());

                        try {
                            JSONObject responseObject = new JSONObject();
                            JSONObject messageJson = new JSONObject();
                            JSONObject jsonConnection = new JSONObject();

                            jsonConnection.put("ConnectionId", connection.getConnectionId());
                            jsonConnection.put("CreationTime", connection.getCreationTime());
                            jsonConnection.put("Data", connection.getData());

                            messageJson.put("ConnectionData", jsonConnection);
                            messageJson.put("MessageBody", data);
                            messageJson.put("MessageType", type );
                            responseObject.put("MessageData", messageJson);
                            sendSuccess(responseObject, Appconstants.SignalReceived);
                        }catch (Exception exp){
                            exp.printStackTrace();
                        }

                    }
                });
                manager.connect(messageBean.getToken());
            }else {
                try {
                    SignalMessage message = new SignalMessage(messageBean.getMessageData().getMessageType(),
                            messageBean.getMessageData().getMessageBody());
                    if (manager != null && messageBean.getMessageData().getConnectionData() != null){
                        MessageBean.ConnectionData data = messageBean.getMessageData().getConnectionData();
//                            long connection_time = Long.parseLong(data.getConnectionId());
                        OpentokConnection  connection = new OpentokConnection(data.getConnectionId(), data.getData(), 0);
                        if (connection != null)
                            sendMessage(message, connection);
                        else
                            sendGroupMessage(message);
//                        sendMessage(message, connection);
//                            sendGroupMessage(message);
                    }
                }catch (Exception exp){
                    exp.printStackTrace();
                }
            }


        }catch (Exception exp){
            exp.printStackTrace();
            return false;
        }

        return true;
    }


    private void sendGroupMessage(SignalMessage message){
        try {
            if (manager != null && manager.isConnected){
                manager.sendSignal(message.getMessage_type(), message.getMessage());
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }


    }

    private void sendMessage(SignalMessage message, Connection connection){
        try {
            if (manager != null && manager.isConnected){
                manager.sendSignal(message.getMessage_type(), message.getMessage(), connection);
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }


    }


    public void pushResultToPlugin(PluginResult.Status status, JSONObject jsonObject){
        try {
//            PluginResult result = new PluginResult(status, jsonObject);
//            mCallBackContext.sendPluginResult(result);

            try {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("data", jsonObject);
                jsonResponse.put("message", "");
                if (status == PluginResult.Status.OK ){
                    jsonResponse.put("status", "success");
                    PluginResult result = new PluginResult(PluginResult.Status.OK, jsonResponse);
                    if (mCallBackContext != null)
                        mCallBackContext.sendPluginResult(result);
                }
                else{
                    jsonResponse.put("status", "failure");
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, jsonResponse);
                    if (mCallBackContext != null)
                        mCallBackContext.sendPluginResult(result);
                }



            }catch (Exception exp){
                exp.printStackTrace();
            }


        }catch (Exception exp){
            exp.printStackTrace();
        }

    }


    /**
     * sends success json to js
     * @param jsonData
     * @param message
     */
    private void sendSuccess(JSONObject jsonData, String message){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", jsonData);
            jsonObject.put("message", message);
            jsonObject.put("status", "success");


            PluginResult result = new PluginResult(PluginResult.Status.OK, jsonObject);
            if (mCallBackContext != null)
                mCallBackContext.sendPluginResult(result);
        }catch (Exception exp){
            exp.printStackTrace();
        }

    }

    /**
     * sends error json to js
     * @param jsonData
     * @param errorMessage
     */
    private void sendError(JSONObject jsonData, String errorMessage){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", jsonData);
            jsonObject.put("message", errorMessage);
            jsonObject.put("status", "failure");


            PluginResult result = new PluginResult(PluginResult.Status.ERROR, jsonObject);
            if (mCallBackContext != null)
                mCallBackContext.sendPluginResult(result);
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @Override
    public void onSignalReceived(Session session, String s, String s1, Connection connection) {
        try {
            JSONObject messageJson = new JSONObject();
            JSONObject jsonConnection = new JSONObject();
            jsonConnection.put("ConnectionId", connection.getConnectionId());
            jsonConnection.put("CreationTime", connection.getCreationTime());
            jsonConnection.put("Data", connection.getData());

            messageJson.put("MessageData", jsonConnection);
            messageJson.put("MessageBody", data);
            messageJson.put("MessageType", type);
            sendSuccess(messageJson, Appconstants.SignalReceived);
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
