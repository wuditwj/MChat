package cn.wildfire.chat.app.shop;

import android.graphics.Color;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.adapter.OrderAdapter;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.shop.entity.BaseTotalPage;
import cn.wildfire.chat.app.shop.entity.OrderInfo;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 订单列表
 */
public class OrderListActivity extends BaseActivity {

    @BindView(R.id.order_list)
    RecyclerView orderList;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartRefreshLayout;

    private int page = 1;
    private OrderAdapter orderAdapter;


    @Override
    protected int contentLayout() {
        return R.layout.activity_order_list;
    }

    protected void init() {
        toolbarTitle.setText("订单列表");
        orderList.setLayoutManager(new LinearLayoutManager(this));
        orderList.addItemDecoration(new ItemDecorationDivider(this, ItemDecorationDivider.VERTICAL_LIST,
                10, Color.parseColor("#F4F4F4")));
        orderAdapter = new OrderAdapter(this, new ArrayList<>());
        orderList.setAdapter(orderAdapter);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getOrder(page);
            refreshLayout.finishRefresh();
        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            getOrder(page);
            refreshLayout.finishLoadMore();
        });
        getOrder(page);
    }

    private void getOrder(int page) {
        ApiMethodFactory.getInstance().getOrderList(ChatManager.Instance().getUserId(), page, 10, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<BaseTotalPage<OrderInfo>> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<BaseTotalPage<OrderInfo>>>() {
                });
                if (obj.getCode() == 200) {
                    if (page == 1) {
                        orderAdapter.setNewData(obj.getData().getOrderList());
                    } else {
                        if (page <= obj.getData().getTotalPage()) {
                            orderAdapter.addNewData(obj.getData().getOrderList());
                        }
                    }
                }
            }
        });
    }
}
