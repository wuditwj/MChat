package cn.wildfirechat.message.notification;

import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.wildfirechat.message.Message;
import cn.wildfirechat.message.core.ContentTag;
import cn.wildfirechat.message.core.MessagePayload;
import cn.wildfirechat.message.core.PersistFlag;
import cn.wildfirechat.remote.ChatManager;

import static cn.wildfirechat.message.core.MessageContentType.ContentType_RED_ENVELOPE_STATUS;

@ContentTag(type = ContentType_RED_ENVELOPE_STATUS, flag = PersistFlag.Persist)
public class RobRedMessageContent extends NotificationMessageContent {
    private String redUid;

    private String receiverId;


    private String fromId;

    private long redTime;
    //红包
    private long reciveTime;

    public RobRedMessageContent(String redUid, String receiverId, String fromId, long redTime, long reciveTime) {
        this.redUid = redUid;
        this.receiverId = receiverId;
        this.fromId = fromId;
        this.redTime = redTime;
        this.reciveTime = reciveTime;
    }

    public long getRedTime() {
        return redTime;
    }

    public void setRedTime(long redTime) {
        this.redTime = redTime;
    }

    public long getReciveTime() {
        return reciveTime;
    }

    public void setReciveTime(long reciveTime) {
        this.reciveTime = reciveTime;
    }

    public String getRedUid() {
        return redUid;
    }

    public void setRedUid(String redUid) {
        this.redUid = redUid;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    @Override
    public MessagePayload encode() {
        MessagePayload payload = new MessagePayload();
        try {
            JSONObject objWrite = new JSONObject();
            objWrite.put("redUid", redUid);
            objWrite.put("receiverId", receiverId);
            objWrite.put("fromId", fromId);
            objWrite.put("redTime", redTime);
            objWrite.put("reciveTime", reciveTime);
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
                redUid = jsonObject.optString("redUid");
                receiverId = jsonObject.optString("receiverId");
                fromId = jsonObject.optString("fromId");
                redTime = jsonObject.optLong("redTime");
                reciveTime = jsonObject.optLong("reciveTime");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String formatNotification(Message message) {
        String notification = "";
//        long second = (reciveTime-redTime);
        if (fromSelf) {
            notification = "您领取了%s的红包";
            notification = String.format(notification, ChatManager.Instance().getUserDisplayName(fromId));
        } else {
            if (TextUtils.equals(ChatManager.Instance().getUserId(), fromId)) {
                if (receiverId.equals(fromId)) {
                    notification = "您领取了自己的红包";
                } else {
                    notification = ChatManager.Instance().getUserDisplayName(receiverId) + "领取了您的红包";
                }
            } else if (TextUtils.equals(receiverId, ChatManager.Instance().getUserId())) {
                notification = "您领取了%s的红包";
                notification = String.format(notification, ChatManager.Instance().getUserDisplayName(fromId));
            }

        }
        return notification;
    }


    public RobRedMessageContent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.redUid);
        dest.writeString(this.receiverId);
        dest.writeString(this.fromId);
        dest.writeLong(this.redTime);
        dest.writeLong(this.reciveTime);
    }

    protected RobRedMessageContent(Parcel in) {
        super(in);
        this.redUid = in.readString();
        this.receiverId = in.readString();
        this.fromId = in.readString();
        this.redTime = in.readLong();
        this.reciveTime = in.readLong();
    }

    public static final Creator<RobRedMessageContent> CREATOR = new Creator<RobRedMessageContent>() {
        @Override
        public RobRedMessageContent createFromParcel(Parcel source) {
            return new RobRedMessageContent(source);
        }

        @Override
        public RobRedMessageContent[] newArray(int size) {
            return new RobRedMessageContent[size];
        }
    };
}
