package cn.wildfire.chat.app.shop.entity;

import java.util.List;

public class GoodsMallVo {
    private List<MallGoodsBannerVo> imageList;
    private GoodsMallInfoVo info;

    public List<MallGoodsBannerVo> getImageList() {
        return imageList;
    }

    public void setImageList(List<MallGoodsBannerVo> imageList) {
        this.imageList = imageList;
    }

    public GoodsMallInfoVo getInfo() {
        return info;
    }

    public void setInfo(GoodsMallInfoVo info) {
        this.info = info;
    }
}
