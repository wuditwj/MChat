package cn.wildfire.chat.app.sysmessage.entity;

public class SystemMessage {
    private long addTime;
    private String content;

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
