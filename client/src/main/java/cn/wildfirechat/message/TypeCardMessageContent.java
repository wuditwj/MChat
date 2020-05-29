package cn.wildfirechat.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import cn.wildfirechat.message.core.ContentTag;
import cn.wildfirechat.message.core.MessagePayload;
import cn.wildfirechat.message.core.PersistFlag;

import static cn.wildfirechat.message.core.MessageContentType.MESSAGE_CONTENT_TYPE_CARD;

/**
 * Created by heavyrain lee on 2017/12/6.
 */

@ContentTag(type = MESSAGE_CONTENT_TYPE_CARD, flag = PersistFlag.Persist_And_Count)
public class TypeCardMessageContent extends MessageContent {
    private String cardUserId;

    public String getCardUserId() {
        return cardUserId;
    }

    public void setCardUserId(String cardUserId) {
        this.cardUserId = cardUserId;
    }

    public TypeCardMessageContent(String cardUserId) {
        this.cardUserId = cardUserId;
    }


    @Override
    public MessagePayload encode() {
        MessagePayload payload = new MessagePayload();
        try {
            JSONObject objWrite = new JSONObject();
            objWrite.put("cardUserId", cardUserId);
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
                cardUserId = jsonObject.optString("cardUserId");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String digest(Message message) {
        return "名片";
    }


    public TypeCardMessageContent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardUserId);
    }

    protected TypeCardMessageContent(Parcel in) {
        this.cardUserId = in.readString();
    }

    public static final Creator<TypeCardMessageContent> CREATOR = new Creator<TypeCardMessageContent>() {
        @Override
        public TypeCardMessageContent createFromParcel(Parcel source) {
            return new TypeCardMessageContent(source);
        }

        @Override
        public TypeCardMessageContent[] newArray(int size) {
            return new TypeCardMessageContent[size];
        }
    };
}
