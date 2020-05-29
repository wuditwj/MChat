package cn.wildfirechat.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import cn.wildfirechat.message.core.ContentTag;
import cn.wildfirechat.message.core.MessagePayload;
import cn.wildfirechat.message.core.PersistFlag;

import static cn.wildfirechat.message.core.MessageContentType.ContentType_RedPackage;

@ContentTag(type = ContentType_RedPackage, flag = PersistFlag.Persist_And_Count)
public class RedPackageMessageContent extends MessageContent {
    private String redTitle;
    private String sendLogId;
    private String sendType;
    private String receiveStatus;
    private String redMoney;
    private String toUserId;
    private String nickName;
    private String formUser;

    public RedPackageMessageContent(String fromUserId, String redTitle, String sendLogId, String sendType, String receiveStatus, String redMoney, String toUserId, String nickName) {
        this.redTitle = redTitle;
        this.sendLogId = sendLogId;
        this.sendType = sendType;
        this.receiveStatus = receiveStatus;
        this.redMoney = redMoney;
        this.toUserId = toUserId;
        this.nickName = nickName;
        this.formUser = fromUserId;
    }

    public String getFormUser() {
        return formUser;
    }

    public void setFormUser(String formUser) {
        this.formUser = formUser;
    }

    public String getSendType() {
        return sendType;
    }

    public String getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(String redMoney) {
        this.redMoney = redMoney;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendLogId() {
        return sendLogId;
    }

    public void setSendLogId(String sendLogId) {
        this.sendLogId = sendLogId;
    }


    public String getRedTitle() {
        return redTitle;
    }

    public void setRedTitle(String redTitle) {
        this.redTitle = redTitle;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    @Override
    public MessagePayload encode() {
        MessagePayload payload = new MessagePayload();
        try {
            JSONObject objWrite = new JSONObject();
            objWrite.put("redTitle", redTitle);
            objWrite.put("sendLogId", sendLogId);
            objWrite.put("sendType", sendType);
            objWrite.put("receiveStatus", receiveStatus);
            objWrite.put("redMoney",redMoney);
            objWrite.put("toUserId",toUserId);
            objWrite.put("nickName",nickName);
            objWrite.put("formUser",formUser);
            payload.content = objWrite.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return payload;

    }


    @Override
    public void decode(MessagePayload payload) {
        try {
            if (payload.content != null) {
                JSONObject jsonObject = new JSONObject(payload.content);
                redTitle = jsonObject.optString("redTitle");
                sendLogId = jsonObject.optString("sendLogId");
                sendType = jsonObject.optString("sendType");//1单发 2 群发
                receiveStatus = jsonObject.optString("receiveStatus");//1.未领取 2.已全部领取
                redMoney = jsonObject.optString("redMoney");
                toUserId = jsonObject.optString("toUserId");
                nickName = jsonObject.optString("nickName");
                formUser =jsonObject.optString("formUser");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String digest(Message message) {
        return redTitle;
    }


    public RedPackageMessageContent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.redTitle);
        dest.writeString(this.sendLogId);
        dest.writeString(this.sendType);
        dest.writeString(this.receiveStatus);
        dest.writeString(this.redMoney);
        dest.writeString(this.toUserId);
        dest.writeString(this.nickName);
        dest.writeString(this.formUser);
    }

    protected RedPackageMessageContent(Parcel in) {
        this.redTitle = in.readString();
        this.sendLogId = in.readString();
        this.sendType = in.readString();
        this.receiveStatus = in.readString();
        this.redMoney = in.readString();
        this.toUserId = in.readString();
        this.nickName = in.readString();
        this.formUser = in.readString();
    }

    public static final Creator<RedPackageMessageContent> CREATOR = new Creator<RedPackageMessageContent>() {
        @Override
        public RedPackageMessageContent createFromParcel(Parcel source) {
            return new RedPackageMessageContent(source);
        }

        @Override
        public RedPackageMessageContent[] newArray(int size) {
            return new RedPackageMessageContent[size];
        }
    };
}

