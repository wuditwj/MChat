package cn.wildfire.chat.app.envelope;

import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zrq.spanbuilder.Spans;

import java.text.DecimalFormat;
import java.util.List;

import cn.wildfire.chat.app.entity.RedInfo;
import cn.wildfirechat.chat.R;

public class ReceiveRedListAdapter extends BaseQuickAdapter<RedInfo, BaseViewHolder> {
    public ReceiveRedListAdapter(int layoutResId, @Nullable List<RedInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RedInfo item) {
        ImageView circleImageView = helper.getView(R.id.header_img);
        Glide.with(getContext()).load(item.get_portrait()).into(circleImageView);
        Spannable spannable = Spans.builder().text(TextUtils.isEmpty(item.getDisplayname()) ? "" : item.getDisplayname(), 15, Color.BLACK)
                .text("\n" + item.getReceive_time(), 12, ContextCompat.getColor(getContext(), R.color.gray7)).build();
        helper.setText(R.id.name, spannable);
        DecimalFormat df = new DecimalFormat("#######0.00");

        helper.setText(R.id.money, df.format(item.getReceive_money()) + "元");
        if (item.getReceive_top() == 2) {
            helper.setVisible(R.id.is_top, true);
        } else {
            helper.setVisible(R.id.is_top, false);
        }
    }
}
