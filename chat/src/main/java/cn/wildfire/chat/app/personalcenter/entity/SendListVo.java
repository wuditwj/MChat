package cn.wildfire.chat.app.personalcenter.entity;

public class SendListVo {
    private int red_number;
    private String send_type;
    private String send_log_id;
    private String yiCount;//已领个数
    private String sendTime;
    private int sendType;
    private String  sendLogId;
    private String userId;
    private int redMoney;

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

    public int getRed_number() {
        return red_number;
    }

    public int getRed_Money() {
        return redMoney;
    }

    public void setRed_Money(int red_Money) {
        this.redMoney = red_Money;
    }

    public void setRed_number(int red_number) {
        this.red_number = red_number;
    }

    public String getSend_type() {
        return send_type;
    }

    public void setSend_type(String send_type) {
        this.send_type = send_type;
    }

    public String getSend_log_id() {
        return send_log_id;
    }

    public void setSend_log_id(String send_log_id) {
        this.send_log_id = send_log_id;
    }

    public String getYiCount() {
        return yiCount;
    }

    public void setYiCount(String yiCount) {
        this.yiCount = yiCount;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
