package cn.wildfire.chat.app.sysmessage.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.app.sysmessage.entity.SystemMessage;
import cn.wildfirechat.chat.R;

public class SystemMessageAdapter extends BaseQuickAdapter<SystemMessage, BaseViewHolder> {
    public SystemMessageAdapter(int layoutResId, @Nullable List<SystemMessage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMessage item) {
        helper.setText(R.id.time, Util.longToString(item.getAddTime(), "yyyy.MM.dd hh:MM:ss"));
        helper.setText(R.id.content, item.getContent());
    }
}
