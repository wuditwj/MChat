package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.SuperTextView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.zhouwei.library.CustomPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.adapter.BillAdapter;
import cn.wildfire.chat.app.personalcenter.adapter.BillMenuAdapter;
import cn.wildfire.chat.app.personalcenter.entity.BaseBill;
import cn.wildfire.chat.app.personalcenter.entity.BillType;
import cn.wildfire.chat.app.shop.utils.GridSpacingItemDecoration;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfirechat.chat.R;

/**
 * 账单
 */
public class BillActivity extends BaseActivity {

    @BindView(R.id.showBill)
    SuperTextView showBill;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    private CustomPopWindow mCustomPopWindow;
    private DisplayMetrics dm;
    private String type = "";
    private String time = "";
    private int page = 1;
    private BaseBean<BaseBill> obj;
    private BillAdapter billAdapter;
    private int mposition = 0;

    private BillMenuAdapter billMenuAdapter;

    @Override
    protected int contentLayout() {
        return R.layout.activity_bill;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("账单");

        Resources resources = this.getResources();
        dm = resources.getDisplayMetrics();
        billMenuAdapter = new BillMenuAdapter(R.layout.item_bill, new ArrayList<>());
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ItemDecorationDivider(this, ItemDecorationDivider.VERTICAL_LIST, 1,
                ContextCompat.getColor(this, R.color.line)));
        billAdapter = new BillAdapter(R.layout.item_bill_list, new ArrayList<>());
        list.setAdapter(billAdapter);
        Date c = Calendar.getInstance().getTime();
        time = Util.ToDateYear(c);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String stringDate = sdf.format(c);
        showBill.setLeftString(stringDate);
        getBillList(page, type, time);//默认显示全部
        showBill.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择类型
                showPopBottom();
            }
        });
        showBill.getLeftIconIV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择时间
                showPicker();
            }
        });
        smartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            smartLayout.setNoMoreData(false);
            getBillList(page, type, time);//默认显示全部
            refreshLayout.finishRefresh();

        });
        smartLayout.setOnLoadMoreListener(refreshLayout -> {
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
            refreshLayout.finishLoadMore();

        });

    }

    private void showPopBottom() {

        int width = dm.widthPixels;
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_layout2, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        handleLogic(contentView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, UIUtils.dip2Px(10), true));
        List<BillType> billTypeList = new ArrayList<>();
        billTypeList.add(new BillType("全部", ""));
        billTypeList.add(new BillType("充值", "1"));
        billTypeList.add(new BillType("提现", "2"));
//        billTypeList.add(new BillType("发红包", "3"));
//        billTypeList.add(new BillType("收红包", "4"));
        billTypeList.add(new BillType("有退款", "5"));
        billMenuAdapter.setNewData(billTypeList);
        billMenuAdapter.setmPosition(mposition);
        type = billTypeList.get(mposition).getType();
        recyclerView.setAdapter(billMenuAdapter);

        billMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                billMenuAdapter.setmPosition(position);
                mposition = position;
                type = billTypeList.get(position).getType();
            }
        });
        //处理popWindow 显示内容
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(width, 550)
                .enableBackgroundDark(false) //弹出popWindow时，背景是否变暗
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        //
                    }
                })
                .create();
        mCustomPopWindow.showAsDropDown(showBill);
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel:
                        if (mCustomPopWindow != null) {
                            mCustomPopWindow.dissmiss();
                        }
                        break;
                    case R.id.ok:
                        page = 1;
                        getBillList(page, type, time);
                        if (mCustomPopWindow != null) {
                            mCustomPopWindow.dissmiss();
                        }
                        break;
                }
            }
        };
        contentView.findViewById(R.id.cancel).setOnClickListener(listener);
        contentView.findViewById(R.id.ok).setOnClickListener(listener);
    }

    private void showPicker() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2017, 0, 1);
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String stringDate = sdf.format(date);
                page = 1;
                time = Util.ToDateYear(date);
                showBill.setLeftString(stringDate);
                getBillList(page, type, time);
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(14)//标题文字大小
                .setTitleText("日期选择")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(BillActivity.this, R.color.colorPrimary))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(BillActivity.this, R.color.colorPrimary))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
        pvTime.show();
    }

    private void deleteUserBill() {
        ApiMethodFactory.getInstance().deleteUserBill(ChatManagerHolder.gChatManager.getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                obj = JSONObject.parseObject(response, new TypeReference<BaseBean<BaseBill>>() {
                });
                if (obj.getCode() == 200) {
                    BillAdapter _adapter = new BillAdapter(R.layout.item_bill_list, new ArrayList<>());
                    list.setAdapter(_adapter);
                } else {

                }
            }
        });
    }

    private void getBillList(int page, String type, String time) {
        ApiMethodFactory.getInstance().billList(ChatManagerHolder.gChatManager.getUserId(),
                type, time, 10, page, new HttpHandler() {
                    @Override
                    public void requestSuccess(String response) {
                        obj = JSONObject.parseObject(response, new TypeReference<BaseBean<BaseBill>>() {
                        });
                        if (obj.getCode() == 200) {
                            if (page == 1) {
                                billAdapter.setNewData(obj.getData().getOrderList());
                                list.setAdapter(billAdapter);
                            } else {
                                billAdapter.addData(obj.getData().getOrderList());

                            }

                        } else {
                            smartLayout.setNoMoreData(true);
                            billAdapter.setNewData(new ArrayList<>());
                        }
                    }
                });
    }

}
