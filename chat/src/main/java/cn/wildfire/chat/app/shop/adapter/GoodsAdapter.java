package cn.wildfire.chat.app.shop.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zrq.spanbuilder.Spans;

import java.util.List;

import butterknife.BindView;
import cn.wildfire.chat.app.shop.GoodDetailActivity;
import cn.wildfire.chat.app.shop.entity.GoodsListInfo;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfirechat.chat.R;

/**
 * 商品列表
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private Context context;
    private List<GoodsListInfo> data;

    public GoodsAdapter(Context context, List<GoodsListInfo> data) {
        this.context = context;
        this.data = data;
    }

    public void addNewData(List<GoodsListInfo> listInfos) {
        data.addAll(listInfos);
    }

    public void setNewData(List<GoodsListInfo> listInfos) {
        data.clear();
        data.addAll(listInfos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodsListInfo goodsListInfo = data.get(position);
        GlideApp.with(context).load(goodsListInfo.getPhoto()).into(holder.goodsImg);
        holder.title.setText(goodsListInfo.getTitle());
        holder.price.setText(Spans.builder().text(Util.decimalFormatMoney(goodsListInfo.getPrice())).deleteLine().build());
        holder.mallPrice.setText(Util.decimalFormatMoney(goodsListInfo.getMall_price()));
        holder.goodsNum.setText("剩余:" + goodsListInfo.getNum());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodDetailActivity.class);
                intent.putExtra("goods_id", goodsListInfo.getGoods_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView goodsImg;
        private TextView title;
        private TextView price;
        private TextView mallPrice;
        private TextView goodsNum;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImg = itemView.findViewById(R.id.goods_img);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            mallPrice = itemView.findViewById(R.id.mall_price);
            goodsNum = itemView.findViewById(R.id.goods_num);

        }
    }

}
