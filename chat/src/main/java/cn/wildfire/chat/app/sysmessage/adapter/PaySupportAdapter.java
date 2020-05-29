package cn.wildfire.chat.app.sysmessage.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.personalcenter.entity.BillVo;
import cn.wildfirechat.chat.R;

public class PaySupportAdapter extends BaseQuickAdapter<BillVo, BaseViewHolder> {
    public PaySupportAdapter(int layoutResId, @Nullable List<BillVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillVo item) {
        helper.setText(R.id.time,item.getCreateTime());
        helper.setText(R.id.price,"￥"+ item.getMoney());
    }
}
