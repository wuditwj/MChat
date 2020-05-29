package cn.wildfire.chat.app.personalcenter.entity;

import java.util.List;

public class BaseBill {
    private int totalPage;
    private List<BillVo> OrderList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<BillVo> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<BillVo> orderList) {
        OrderList = orderList;
    }
}
