package cn.wildfire.chat.app.personalcenter.adapter;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.aries.ui.view.radius.RadiusTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfirechat.chat.R;

public class RechargeMoneyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int mPosition = -1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
        notifyDataSetChanged();
    }

    public RechargeMoneyAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        RadiusTextView name  = helper.getView(R.id.name);
        name.setText(item+"元");
        if (mPosition==helper.getAdapterPosition()){
            name.getDelegate().setBackgroundColor(Color.parseColor("#F36292"));
            name.setTextColor(Color.WHITE);
        }else {
            name.getDelegate().setBackgroundColor(Color.WHITE);
            name.setTextColor(Color.BLACK);
        }

    }
}
