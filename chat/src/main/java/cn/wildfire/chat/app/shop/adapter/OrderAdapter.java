package cn.wildfire.chat.app.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.wildfire.chat.app.shop.entity.GoodsListInfo;
import cn.wildfire.chat.app.shop.entity.OrderInfo;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfirechat.chat.R;

/**
 * 我的订单列表
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderInfo> data;

    public OrderAdapter(Context context, List<OrderInfo> data) {
        this.context = context;
        this.data = data;
    }

    public void addNewData(List<OrderInfo> listInfo) {
        data.addAll(listInfo);
    }

    public void setNewData(List<OrderInfo> listInfo) {
        data.clear();
        data.addAll(listInfo);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderInfo orderInfo = data.get(position);
        holder.orderNo.setText("订单编号:"+orderInfo.getId());
        GlideApp.with(context).load(orderInfo.getPhoto()).into(holder.goodImg);
        holder.name.setText(orderInfo.getName());
        holder.price.setText(Util.decimalFormatMoney(orderInfo.getPrice()));
        holder.number.setText("x"+orderInfo.getNum());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNo;
        ImageView goodImg;
        TextView name;
        TextView price;
        TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            orderNo = itemView.findViewById(R.id.order_no);
            goodImg = itemView.findViewById(R.id.goods_img);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            number = itemView.findViewById(R.id.number);
        }
    }
}
