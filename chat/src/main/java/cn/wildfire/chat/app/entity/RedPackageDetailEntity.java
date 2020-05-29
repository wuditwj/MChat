package cn.wildfire.chat.app.entity;

import java.util.List;

public class RedPackageDetailEntity {
    private double money;
    private  RedInfo title;
    private List<RedInfo> list;

    public List<RedInfo> getList() {
        return list;
    }

    public void setList(List<RedInfo> list) {
        this.list = list;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public RedInfo getTitle() {
        return title;
    }

    public void setTitle(RedInfo title) {
        this.title = title;
    }
}
