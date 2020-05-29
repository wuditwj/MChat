package cn.wildfire.chat.app.sysmessage;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.entity.BaseBill;
import cn.wildfire.chat.app.shop.utils.GridSpacingItemDecoration;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.app.sysmessage.adapter.PaySupportAdapter;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.chat.R;

/**
 * 支付小助手
 */
public class PaySupport extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    private String type = "1";
    private String time = "";
    private int page = 1;
    private BaseBean<BaseBill> obj;
    private PaySupportAdapter paySupportAdapter;

    @Override
    protected int contentLayout() {
        return R.layout.activity_pay_support;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("支付小助手");

        ClassicsFooter.REFRESH_FOOTER_PULLING = "下拉加载更多...";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_PULLING = "上拉可以刷新...";
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);
        list.addItemDecoration(new GridSpacingItemDecoration(1, 20, true));
        paySupportAdapter = new PaySupportAdapter(R.layout.item_recharge_notify, new ArrayList<>());
        list.setAdapter(paySupportAdapter);
        Date c = Calendar.getInstance().getTime();
        time = Util.ToDateYear(c);
        getBillList(page, type, time);//默认显示全部
        smartLayout.setOnRefreshListener(refreshLayout -> {

            page++;
            if (obj.getData() != null) {
                if (page <= obj.getData().getTotalPage()) {
                    getBillList(page, type, time);//加载更多
                } else {
                    smartLayout.setNoMoreData(true);
                }
            } else {
                smartLayout.setNoMoreData(true);
            }
            refreshLayout.finishRefresh();

        });
        smartLayout.setOnLoadMoreListener(refreshLayout -> {
            page = 1;
            smartLayout.setNoMoreData(false);
            getBillList(page, type, time);//默认显示全部
            refreshLayout.finishLoadMore();

        });
    }

    private void getBillList(int page, String type, String time) {
        ApiMethodFactory.getInstance().billList(ChatManagerHolder.gChatManager.getUserId(), type, time, 10, page, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                obj = JSONObject.parseObject(response, new TypeReference<BaseBean<BaseBill>>() {
                });
                if (obj.getCode() == 200) {
                    Collections.reverse(obj.getData().getOrderList());
                    if (page == 1) {
                        paySupportAdapter.setNewData(obj.getData().getOrderList());
                        list.setAdapter(paySupportAdapter);
                    } else {
                        paySupportAdapter.addData(obj.getData().getOrderList());

                    }

                } else {
                    smartLayout.setNoMoreData(true);
                }
            }
        });
    }

}
