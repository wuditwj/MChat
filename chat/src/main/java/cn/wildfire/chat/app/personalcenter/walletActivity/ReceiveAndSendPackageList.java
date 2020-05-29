package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.CircleImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zrq.spanbuilder.Spans;
import com.zrq.spanbuilder.TextStyle;

import java.text.DecimalFormat;

import butterknife.BindView;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.envelope.RedPackageDetailActivity;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.adapter.ReceiveAdapter;
import cn.wildfire.chat.app.personalcenter.adapter.SendAdapter;
import cn.wildfire.chat.app.personalcenter.entity.ReceiveRedVo;
import cn.wildfire.chat.app.personalcenter.entity.SendRedVo;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 红包记录/我的红包
 */
public class ReceiveAndSendPackageList extends BaseActivity {

    @BindView(R.id.header_img)
    CircleImageView headerImg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.receive_count)
    TextView receive_count;
    @BindView(R.id.receive_count_top)
    TextView receiveCountTop;
    @BindView(R.id.send_number)
    TextView sendNumber;
    @BindView(R.id.header)
    LinearLayout header;
    @BindView(R.id.receive_money)
    TextView receiveMoney;
    @BindView(R.id.receive_layout)
    LinearLayout receiveLayout;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.send_money)
    TextView send_money;
    @BindView(R.id.tab_layout)
    SegmentTabLayout segmentTabLayout;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smart_layout;

    private int page = 1;
    private ReceiveAdapter receiveAdapter;

    private SendAdapter sendAdapter;
    private String[] mTitles = {"收到的红包", "发出去的红包"};
    private int mPosition = 0;
    DecimalFormat numberFormat = new DecimalFormat("￥#######0.00");

    private int flag = 0;

    @Override
    protected int contentLayout() {
        return R.layout.activity_receive_and_send_package_list;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("我的红包");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDecorationDivider(this,
                ItemDecorationDivider.VERTICAL_LIST, 1, ContextCompat.getColor(this, R.color.gray8)));
        getPackage(page);//获得红包列表   默认
        sendPackage(page, true);
        segmentTabLayout.setTabData(mTitles);
        segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mPosition = position;
                page = 1;
                if (position == 0) {
//                    Toast.makeText(ReceiveAndSendPackageList.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    getPackage(page);
                } else {
//                    Toast.makeText(ReceiveAndSendPackageList.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    sendPackage(page, true);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        smart_layout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            if (mPosition == 0) {
                getPackage(page);
            } else {
                sendPackage(page, true);
            }
            refreshLayout.finishRefresh();
        });
        smart_layout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            if (mPosition == 0) {
                getPackage(page);
            } else {
                sendPackage(page, true);
            }
            refreshLayout.finishLoadMore();
        });

    }

    private void getPackage(int page) {

        ApiMethodFactory.getInstance().myReceivedRed(ChatManager.Instance().getUserId(), page, 10, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<ReceiveRedVo> baseBean = JSONObject.parseObject(response, new TypeReference<BaseBean<ReceiveRedVo>>() {
                });
                if (baseBean.getCode() == 200) {
                    name.setVisibility(View.VISIBLE);
                    name.setText(baseBean.getData().getTitle().getDisplayname());

                    setParam(baseBean.getData().getTitle().get_portrait(), baseBean.getData().getCount(), String.valueOf(baseBean.getData().getTopCount()), numberFormat.format(baseBean.getData().getSumMoney()));
                    flag++;
                    if (receiveAdapter == null) {
                        receiveAdapter = new ReceiveAdapter(R.layout.item_send_package, baseBean.getData().getOrderList());
                        recyclerView.setAdapter(receiveAdapter);

                    } else {
                        if (page == 1) {
                            receiveAdapter.setNewData(baseBean.getData().getOrderList());
                            recyclerView.setAdapter(receiveAdapter);
                        } else {
                            if (page < baseBean.getData().getTotalPage()) {
                                receiveAdapter.addData(baseBean.getData().getOrderList());
                                receiveAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    receiveAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent(ReceiveAndSendPackageList.this, RedPackageDetailActivity.class);
                            intent.putExtra("sendLogId", receiveAdapter.getData().get(position).getSendLogId());
                            intent.putExtra("robUser", receiveAdapter.getData().get(position).getUserId());
                            intent.putExtra("red_Type", receiveAdapter.getData().get(position).getSendType());
                            startActivity(intent);
                        }
                    });

                }
            }
        });

    }

    private void setParam(String headImg, String receiveCount, String receiveCountTop, String receiveMoney) {
        if (flag == 0) {
            if (!TextUtils.isEmpty(headImg)) {
                Glide.with(ReceiveAndSendPackageList.this)
                        .load(headImg)
                        .into(headerImg);
            }
            receive_count.setText(receiveCount);
            this.receiveCountTop.setText(receiveCountTop);
            this.receiveMoney.setText(receiveMoney);
        }
    }

    private void sendPackage(int page, boolean isShow) {
        ApiMethodFactory.getInstance().mySendRed(ChatManager.Instance().getUserId(), page, 10, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<SendRedVo> baseBean = JSONObject.parseObject(response, new TypeReference<BaseBean<SendRedVo>>() {
                });
                if (baseBean.getCode() == 200) {
                    name.setText(baseBean.getData().getTitle().getDisplayname());
                    GlideApp.with(ReceiveAndSendPackageList.this).load(baseBean.getData().getTitle().get_portrait())
                            .into(headerImg);
                    send_money.setText(numberFormat.format(baseBean.getData().getSumMoney()));
                    Log.i("--==>>", "onSuccess: " + numberFormat.format(baseBean.getData().getSumMoney()));
                    sendNumber.setText(baseBean.getData().getCount());

                    if (isShow) {
                        if (sendAdapter == null) {
                            sendAdapter = new SendAdapter(R.layout.item_send_package_1, baseBean.getData().getOrderList());
                            recyclerView.setAdapter(receiveAdapter);
                        } else {
                            if (page == 1) {
                                sendAdapter.setNewData(baseBean.getData().getOrderList());
                                recyclerView.setAdapter(sendAdapter);
                            } else {
                                if (page < baseBean.getData().getTotalPage()) {
                                    sendAdapter.addData(baseBean.getData().getOrderList());
                                    sendAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        sendAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(ReceiveAndSendPackageList.this, RedPackageDetailActivity.class);
                                intent.putExtra("sendLogId", sendAdapter.getData().get(position).getSendLogId());
                                intent.putExtra("robUser", sendAdapter.getData().get(position).getUserId());
                                intent.putExtra("red_Type", sendAdapter.getData().get(position).getSendType());
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }


    private Spannable spannable1(String s, String s2, String color1) {
        Spannable spannable = Spans.builder().text(s, 13, ContextCompat.getColor(this, R.color.black))
                .text(s2, 14, Color.parseColor(color1))
                .style(TextStyle.BOLD)
                .build();
        return spannable;
    }

}
