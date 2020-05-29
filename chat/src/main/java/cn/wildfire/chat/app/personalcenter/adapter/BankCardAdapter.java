package cn.wildfire.chat.app.personalcenter.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.personalcenter.entity.CardVo;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfirechat.chat.R;

public class BankCardAdapter extends BaseQuickAdapter<CardVo, BaseViewHolder> {
    public BankCardAdapter(int layoutResId, @Nullable List<CardVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardVo item) {
        ImageView bankLogo = helper.getView(R.id.bank_logo);
        GlideApp.with(getContext()).load(item.getBank_logo())
                .into(bankLogo);
        helper.setText(R.id.bank_name, item.getBank());

        helper.setText(R.id.card_no, Util.replace(Util.hideCardNo(item.getBank_account())));
    }
}