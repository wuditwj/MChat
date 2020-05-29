package cn.wildfire.chat.app.shop.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.shop.entity.AddressInfo;
import cn.wildfirechat.chat.R;

public class AddressAdapter extends BaseQuickAdapter<AddressInfo, BaseViewHolder> {

    public AddressAdapter(int layoutResId, List<AddressInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressInfo item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.mobile, item.getMobile());
        helper.setText(R.id.address, item.getAreaName() + item.getAddr());
    }
}
