package cn.wildfirechat.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import cn.wildfirechat.message.core.ContentTag;
import cn.wildfirechat.message.core.MessagePayload;
import cn.wildfirechat.message.core.PersistFlag;

import static cn.wildfirechat.message.core.MessageContentType.MESSAGE_CONTENT_TYPE_FINGER;

/**
 * Created by heavyrain lee on 2017/12/6.
 */

@ContentTag(type = MESSAGE_CONTENT_TYPE_FINGER, flag = PersistFlag.Persist_And_Count)
public class FingerMessageContent extends MessageContent {
    private String imageName;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public FingerMessageContent(String imageName) {
        this.imageName = imageName;
    }


    @Override
    public MessagePayload encode() {
        MessagePayload payload = new MessagePayload();
        try {
            JSONObject objWrite = new JSONObject();
            objWrite.put("imageName", imageName);
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
                imageName = jsonObject.optString("imageName");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String digest(Message message) {
        return "猜拳";
    }


    public FingerMessageContent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageName);
    }

    protected FingerMessageContent(Parcel in) {
        this.imageName = in.readString();
    }

    public static final Creator<FingerMessageContent> CREATOR = new Creator<FingerMessageContent>() {
        @Override
        public FingerMessageContent createFromParcel(Parcel source) {
            return new FingerMessageContent(source);
        }

        @Override
        public FingerMessageContent[] newArray(int size) {
            return new FingerMessageContent[size];
        }
    };
}
