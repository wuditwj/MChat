package cn.wildfire.chat.app.personalcenter.entity;

import java.util.List;

public class ReceiveRedVo {

    private double sumMoney;
    private int totalPage;
    private int topCount;//手气最佳次数
    private String count;//总共收到
    private List<ReceiveListVo> OrderList;
    private RedUser title;

    public int getTopCount() {
        return topCount;
    }

    public void setTopCount(int topCount) {
        this.topCount = topCount;
    }

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

    public List<ReceiveListVo> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<ReceiveListVo> orderList) {
        OrderList = orderList;
    }

    public RedUser getTitle() {
        return title;
    }

    public void setTitle(RedUser title) {
        this.title = title;
    }
}
