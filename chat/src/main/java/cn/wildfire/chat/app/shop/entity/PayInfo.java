package cn.wildfire.chat.app.shop.entity;

public class PayInfo {
    private String name;
    private String subName;
    private Integer payIcon;
    private String type;

    public PayInfo(String name, String subName, Integer payIcon, String type) {
        this.name = name;
        this.subName = subName;
        this.payIcon = payIcon;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public Integer getPayIcon() {
        return payIcon;
    }

    public void setPayIcon(Integer payIcon) {
        this.payIcon = payIcon;
    }
}
