package cn.wildfire.chat.app.shop.entity;

import java.util.List;

public class MallSubmitVo {
    private String addressId;
    private List<GoodsCartVo> goodsCartVos;
    private String beizhu;
    private String user_id;
    private String   grade ;
    private String goods_id;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<GoodsCartVo> getGoodsCartVos() {
        return goodsCartVos;
    }

    public void setGoodsCartVos(List<GoodsCartVo> goodsCartVos) {
        this.goodsCartVos = goodsCartVos;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
}
