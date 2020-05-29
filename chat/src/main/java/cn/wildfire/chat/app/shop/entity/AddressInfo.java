package cn.wildfire.chat.app.shop.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressInfo implements Parcelable {
    private String addrId;
    private String name;
    private String mobile;
    private String areaName;
    private String addr;

    public String getAddrId() {
        return addrId;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addrId);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.areaName);
        dest.writeString(this.addr);
    }

    public AddressInfo() {
    }

    protected AddressInfo(Parcel in) {
        this.addrId = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.areaName = in.readString();
        this.addr = in.readString();
    }

    public static final Creator<AddressInfo> CREATOR = new Creator<AddressInfo>() {
        @Override
        public AddressInfo createFromParcel(Parcel source) {
            return new AddressInfo(source);
        }

        @Override
        public AddressInfo[] newArray(int size) {
            return new AddressInfo[size];
        }
    };
}
