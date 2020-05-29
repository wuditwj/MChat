package cn.wildfire.chat.app.shop.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsCartVo implements Parcelable {
    private double mall_price;
    private String goods_car_id;
    private int num;
    private String photo;
    private String title;
    private String goods_id;
    private String info;//规格

    public double getMall_price() {
        return mall_price;
    }

    public void setMall_price(double mall_price) {
        this.mall_price = mall_price;
    }

    public String getGoods_car_id() {
        return goods_car_id;
    }

    public void setGoods_car_id(String goods_car_id) {
        this.goods_car_id = goods_car_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mall_price);
        dest.writeString(this.goods_car_id);
        dest.writeInt(this.num);
        dest.writeString(this.photo);
        dest.writeString(this.title);
        dest.writeString(this.goods_id);
        dest.writeString(this.info);
    }

    public GoodsCartVo() {
    }

    protected GoodsCartVo(Parcel in) {
        this.mall_price = in.readDouble();
        this.goods_car_id = in.readString();
        this.num = in.readInt();
        this.photo = in.readString();
        this.title = in.readString();
        this.goods_id = in.readString();
        this.info = in.readString();
    }

    public static final Creator<GoodsCartVo> CREATOR = new Creator<GoodsCartVo>() {
        @Override
        public GoodsCartVo createFromParcel(Parcel source) {
            return new GoodsCartVo(source);
        }

        @Override
        public GoodsCartVo[] newArray(int size) {
            return new GoodsCartVo[size];
        }
    };
}
