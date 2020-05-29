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
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.utils.GridSpacingItemDecoration;
import cn.wildfire.chat.app.sysmessage.adapter.SystemMessageAdapter;
import cn.wildfire.chat.app.sysmessage.entity.SystemBill;
import cn.wildfirechat.chat.R;

/**
 * 萌聊小助手
 */
public class SystemBotMessage extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    private int page = 1;
    private SystemMessageAdapter systemMessageAdapter;

    @Override
    protected int contentLayout() {
        return R.layout.activity_system_bot_message;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("萌聊小助手");

        ClassicsFooter.REFRESH_FOOTER_PULLING = "下拉加载更多...";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在刷新...";
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);
        list.addItemDecoration(new GridSpacingItemDecoration(1, 20, true));
        systemMessageAdapter = new SystemMessageAdapter(R.layout.iten_system_bot, new ArrayList<>());
        list.setAdapter(systemMessageAdapter);
        getBillMessageList(page);
        smartLayout.setOnRefreshListener(refreshLayout -> {

            page++;
            refreshLayout.finishRefresh();
            getBillMessageList(page);
        });
        smartLayout.setOnLoadMoreListener(refreshLayout -> {

            page = 1;
            smartLayout.setNoMoreData(false);
            refreshLayout.finishLoadMore();
        });

    }

    private void getBillMessageList(int page) {

        ApiMethodFactory.getInstance().sysMessageList(page, 10, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<SystemBill> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<SystemBill>>() {
                });

                if (obj.getCode() == 200) {
                    Collections.reverse(obj.getData().getOrderList());
                    if (page == 1) {
                        systemMessageAdapter.setNewData(obj.getData().getOrderList());
                        smartLayout.setNoMoreData(false);
                    } else {
                        if (page <= obj.getData().getTotalPage()) {
                            smartLayout.setNoMoreData(false);
                            systemMessageAdapter.addData(0, obj.getData().getOrderList());
                        } else {
                            smartLayout.setNoMoreData(true);
                        }


                    }

                } else {
                    smartLayout.setNoMoreData(true);
                }
            }
        });
    }

}
