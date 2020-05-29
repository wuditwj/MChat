package cn.wildfire.chat.app.personalcenter.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.personalcenter.entity.BillVo;
import cn.wildfirechat.chat.R;

public class BillAdapter extends BaseQuickAdapter<BillVo, BaseViewHolder> {
    public BillAdapter(int layoutResId, @Nullable List<BillVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillVo item) {

        helper.setText(R.id.time, item.getCreateTime());

//        1 充值 2 提现 3发红包 4 收红包 5退款 type
        switch (item.getType()) {
            case "1"://充值  +
                helper.setText(R.id.info, item.getInfo());
                helper.setTextColor(R.id.amount, ContextCompat.getColor(getContext(), R.color.colorPrimary));
                helper.setText(R.id.amount, item.getMoney());
                break;
            case "2"://提现 -
                if (TextUtils.equals("1", item.getStatus())) {
                    helper.setText(R.id.info, item.getInfo() + "(处理中)");
                } else if (TextUtils.equals("2", item.getStatus())) {
                    helper.setText(R.id.info, item.getInfo() + "(完成)");
                } else {
                    helper.setText(R.id.info, item.getInfo() + "(拒绝)");
                }

                helper.setText(R.id.amount, item.getMoney());
                helper.setTextColor(R.id.amount, ContextCompat.getColor(getContext(), R.color.green1));
                break;
            case "3"://
                helper.setText(R.id.info, item.getInfo());// -
                helper.setText(R.id.amount, item.getMoney());
                helper.setTextColor(R.id.amount, ContextCompat.getColor(getContext(), R.color.black));
                break;
            case "4"://    +
            case "5":
                helper.setText(R.id.info, item.getInfo());
                helper.setText(R.id.amount, item.getMoney());
                helper.setTextColor(R.id.amount, ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;

        }
    }
}
