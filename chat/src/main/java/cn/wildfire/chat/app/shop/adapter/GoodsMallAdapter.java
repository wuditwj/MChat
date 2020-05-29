package cn.wildfire.chat.app.shop.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.app.shop.entity.GoodsCartVo;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfirechat.chat.R;


/**
 * 填写订单信息页的商品列表
 */
public class GoodsMallAdapter extends BaseQuickAdapter<GoodsCartVo, BaseViewHolder> {


    public GoodsMallAdapter(int layoutResId, @Nullable List<GoodsCartVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsCartVo item) {
        helper.setText(R.id.name, item.getTitle());
        helper.setText(R.id.price, Util.decimalFormatMoney(item.getMall_price()));
        ImageView goodsImg = helper.getView(R.id.goods_img);
        helper.setText(R.id.number, "x" + item.getNum());
        GlideApp.with(getContext()).load(item.getPhoto()).into(goodsImg);
        helper.setText(R.id.info, TextUtils.isEmpty(item.getInfo()) ? "" : item.getInfo());
    }
}
