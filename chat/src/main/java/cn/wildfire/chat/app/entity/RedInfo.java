package cn.wildfire.chat.app.entity;

public class RedInfo {
    private String displayname;
    private String red_title;
    private int red_number;
    private String _portrait;
    private double red_money;
    private int receive_top;//手气最佳

    public int getReceive_top() {
        return receive_top;
    }

    public void setReceive_top(int receive_top) {
        this.receive_top = receive_top;
    }

    private double receive_money;//领取金额
    private String receive_time;//领取时间

    public void setRed_money(double red_money) {
        this.red_money = red_money;
    }

    public double getReceive_money() {
        return receive_money;
    }

    public void setReceive_money(double receive_money) {
        this.receive_money = receive_money;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getRed_title() {
        return red_title;
    }

    public void setRed_title(String red_title) {
        this.red_title = red_title;
    }

    public int getRed_number() {
        return red_number;
    }

    public void setRed_number(int red_number) {
        this.red_number = red_number;
    }

    public String get_portrait() {
        return _portrait;
    }

    public void set_portrait(String _portrait) {
        this._portrait = _portrait;
    }

    public double getRed_money() {
        return red_money;
    }
}
