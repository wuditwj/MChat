package cn.wildfire.chat.app.personalcenter.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.personalcenter.entity.BillType;
import cn.wildfirechat.chat.R;

public class BillMenuAdapter extends BaseQuickAdapter<BillType, BaseViewHolder> {
    private int mPosition = 0 ;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
        notifyDataSetChanged();
    }

    public BillMenuAdapter(int layoutResId, @Nullable List<BillType> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillType item) {
        TextView name = helper.getView(R.id.name);

        RadiusRelativeLayout root=helper.getView(R.id.root);

        ImageView choose=helper.getView(R.id.choose);
        name.setText(item.getName());

        if (mPosition == helper.getAdapterPosition()){
            root.getDelegate().setBackgroundColor(Color.parseColor("#FFFFE8F0"));
            choose.setVisibility(View.VISIBLE);
        }else {
            root.getDelegate().setBackgroundColor(Color.parseColor("#FFEEEEEE"));
            choose.setVisibility(View.GONE);

        }
    }
}
