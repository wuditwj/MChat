package cn.wildfire.chat.app.personalcenter.entity;

public class ReceiveListVo {
   private String  displayname;
   private String send_type;
    private String receive_money;
    private String receive_time;
    private int sendType;
    private String  sendLogId;
    private String userId;

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getSendLogId() {
        return sendLogId;
    }

    public void setSendLogId(String sendLogId) {
        this.sendLogId = sendLogId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getSend_type() {
        return send_type;
    }

    public void setSend_type(String send_type) {
        this.send_type = send_type;
    }

    public String getReceive_money() {
        return receive_money;
    }

    public void setReceive_money(String receive_money) {
        this.receive_money = receive_money;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }
}
