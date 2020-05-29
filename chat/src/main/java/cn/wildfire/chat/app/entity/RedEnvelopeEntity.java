package cn.wildfire.chat.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class RedEnvelopeEntity implements Parcelable {
    private String redTitle;
    private String redMoney;
    private String sendLogId;
    private String formUser;
    private String sendType;
    private String receiveStatus;
    private String receiveId;
    private String toUser;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getRedTitle() {
        return redTitle;
    }

    public void setRedTitle(String redTitle) {
        this.redTitle = redTitle;
    }

    public String getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(String redMoney) {
        this.redMoney = redMoney;
    }

    public String getSendLogId() {
        return sendLogId;
    }

    public void setSendLogId(String sendLogId) {
        this.sendLogId = sendLogId;
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

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.redTitle);
        dest.writeString(this.redMoney);
        dest.writeString(this.sendLogId);
        dest.writeString(this.formUser);
        dest.writeString(this.sendType);
        dest.writeString(this.receiveStatus);
        dest.writeString(this.receiveId);
        dest.writeString(this.toUser);
        dest.writeString(this.nickName);
    }

    public RedEnvelopeEntity() {
    }

    protected RedEnvelopeEntity(Parcel in) {
        this.redTitle = in.readString();
        this.redMoney = in.readString();
        this.sendLogId = in.readString();
        this.formUser = in.readString();
        this.sendType = in.readString();
        this.receiveStatus = in.readString();
        this.receiveId = in.readString();
        this.toUser = in.readString();
        this.nickName = in.readString();
    }

    public static final Creator<RedEnvelopeEntity> CREATOR = new Creator<RedEnvelopeEntity>() {
        @Override
        public RedEnvelopeEntity createFromParcel(Parcel source) {
            return new RedEnvelopeEntity(source);
        }

        @Override
        public RedEnvelopeEntity[] newArray(int size) {
            return new RedEnvelopeEntity[size];
        }
    };
}

