package cn.wildfire.chat.app.shop.entity;

import java.util.List;

public class BaseTotalPage<T> {
    private int totalPage;
    private List<T>OrderList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<T> orderList) {
        OrderList = orderList;
    }
}
