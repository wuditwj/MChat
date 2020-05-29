package cn.wildfire.chat.app.sysmessage.entity;

import java.util.List;


public class SystemBill {
    private int totalPage;
    private List<SystemMessage> OrderList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<SystemMessage> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<SystemMessage> orderList) {
        OrderList = orderList;
    }
}
