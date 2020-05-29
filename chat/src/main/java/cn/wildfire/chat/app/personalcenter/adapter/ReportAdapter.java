package cn.wildfire.chat.app.personalcenter.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfirechat.chat.R;

/**
 * 举报投诉
 */
public class ReportAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ReportAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.name, item);
    }
}
