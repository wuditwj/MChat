package cn.wildfire.chat.app.personalcenter.entity;

import java.util.List;

public class SendRedVo {
    private double sumMoney;
    private int totalPage;
    private String count;//发出个多少包;
    private List<SendListVo> OrderList;
    private RedUser title;

    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<SendListVo> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<SendListVo> orderList) {
        OrderList = orderList;
    }

    public RedUser getTitle() {
        return title;
    }

    public void setTitle(RedUser title) {
        this.title = title;
    }
}
