package cn.wildfire.chat.app.personalcenter.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.personalcenter.entity.ReceiveListVo;
import cn.wildfirechat.chat.R;

public class ReceiveAdapter extends BaseQuickAdapter<ReceiveListVo, BaseViewHolder> {
    public ReceiveAdapter(int layoutResId, @Nullable List<ReceiveListVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReceiveListVo item) {


        helper.setText(R.id.key,item.getDisplayname());
        helper.setText(R.id.time,item.getReceive_time());
        helper.setText(R.id.value,item.getReceive_money()+"元");
    }
}
