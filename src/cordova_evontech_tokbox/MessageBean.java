package cordova_evontech_tokbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amitrai on 14/9/17.
 * User for :-
 */

public class MessageBean {

    @SerializedName("ApiKey")
    @Expose
    private String apiKey;
    @SerializedName("SessionId")
    @Expose
    private String sessionId;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("ProfileImage")
    @Expose
    private String profileImage;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("MessageData")
    @Expose
    private MessageData messageData;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }


    public class MessageData {

        @SerializedName("MessageBody")
        @Expose
        private String messageBody;
        @SerializedName("MessageType")
        @Expose
        private String messageType;
        @SerializedName("ConnectionData")
        @Expose
        private ConnectionData connectionData;

        public String getMessageBody() {
            return messageBody;
        }

        public void setMessageBody(String messageBody) {
            this.messageBody = messageBody;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public ConnectionData getConnectionData() {
            return connectionData;
        }

        public void setConnectionData(ConnectionData connectionData) {
            this.connectionData = connectionData;
        }

    }



    public class ConnectionData {

        @SerializedName("connectionId")
        @Expose
        private String connectionId;
        @SerializedName("creationTime")
        @Expose
        private String creationTime;
        @SerializedName("data")
        @Expose
        private String data;

        public String getConnectionId() {
            return connectionId;
        }

        public void setConnectionId(String connectionId) {
            this.connectionId = connectionId;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }


}
