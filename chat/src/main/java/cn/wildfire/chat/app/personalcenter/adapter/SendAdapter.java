package cn.wildfire.chat.app.personalcenter.adapter;

import android.graphics.Color;
import android.text.Spannable;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zrq.spanbuilder.Spans;

import java.util.List;

import cn.wildfire.chat.app.personalcenter.entity.SendListVo;
import cn.wildfirechat.chat.R;

public class SendAdapter extends BaseQuickAdapter<SendListVo, BaseViewHolder> {
    public SendAdapter(int layoutResId, @Nullable List<SendListVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendListVo item) {
        String name = "";
        if (item.getSendType() == 1) {//单发
            name = "普通红包";
        } else if (item.getSendType() == 2) {
            //群发
            name = "拼手气红包";
        } else {
            name = "专属红包";
        }
        Spannable spannable = Spans.builder().text(name, 17, Color.BLACK)
                .text("\n" + item.getSendTime(), 12, ContextCompat.getColor(getContext(), R.color.gray7)).build();

        Spannable spannable1 = Spans.builder().text(String.format("%.2f", Double.parseDouble(String.valueOf(item.getRed_Money()))) + "元", 14, Color.GRAY)
                .text("\n已领" + item.getYiCount() + "/" + item.getRed_number(), 12, ContextCompat.getColor(getContext(), R.color.gray7)).build();
//                  .text(String.format("%.2f", Double.parseDouble(String.valueOf(item.getRed_Money()))) + "元",14, ContextCompat.getColor(mContext, R.color.gray7)).build();
        helper.setText(R.id.key, name);
        helper.setText(R.id.time, item.getSendTime());
        helper.setText(R.id.value1, String.format("%.2f", Double.parseDouble(String.valueOf(item.getRed_Money()))) + "元");
        helper.setText(R.id.value2, "已领" + item.getYiCount() + "/" + item.getRed_number());
    }
}
