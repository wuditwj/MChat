package cn.wildfire.chat.app.shop.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;


import java.util.List;

import cn.wildfire.chat.app.shop.entity.PayInfo;
import cn.wildfirechat.chat.R;

public class PayAdapter extends BaseQuickAdapter<PayInfo, BaseViewHolder> {
    private int position = 0;
    public PayAdapter(int layoutResId, @Nullable List<PayInfo> data) {
        super(layoutResId, data);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, PayInfo item) {
        helper.setText(R.id.name,item.getName());
        helper.setText(R.id.name_sub,item.getSubName());
        helper.setBackgroundResource(R.id.image_recourse,item.getPayIcon());
        ImageView icon = helper.getView(R.id.status);
        if (position==helper.getAdapterPosition()){
            icon.setBackgroundResource(R.mipmap.ic_check_selected);
        }else {
            icon.setBackgroundResource(R.mipmap.ic_check_normal);
        }
    }
}
