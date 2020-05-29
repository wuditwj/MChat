package cn.wildfire.chat.app.personalcenter.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CardVo implements Parcelable {
    private String account_opening;
    private String bank;
    private long createtime;
    private String id;
    private String userid;
    private String branch;
    private String bank_account;
    private String bank_logo;

    public String getBank_logo() {
        return bank_logo;
    }

    public void setBank_logo(String bank_logo) {
        this.bank_logo = bank_logo;
    }

    public String getAccount_opening() {
        return account_opening;
    }

    public void setAccount_opening(String account_opening) {
        this.account_opening = account_opening;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public CardVo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.account_opening);
        dest.writeString(this.bank);
        dest.writeLong(this.createtime);
        dest.writeString(this.id);
        dest.writeString(this.userid);
        dest.writeString(this.branch);
        dest.writeString(this.bank_account);
        dest.writeString(this.bank_logo);
    }

    protected CardVo(Parcel in) {
        this.account_opening = in.readString();
        this.bank = in.readString();
        this.createtime = in.readLong();
        this.id = in.readString();
        this.userid = in.readString();
        this.branch = in.readString();
        this.bank_account = in.readString();
        this.bank_logo = in.readString();
    }

    public static final Creator<CardVo> CREATOR = new Creator<CardVo>() {
        @Override
        public CardVo createFromParcel(Parcel source) {
            return new CardVo(source);
        }

        @Override
        public CardVo[] newArray(int size) {
            return new CardVo[size];
        }
    };
}
