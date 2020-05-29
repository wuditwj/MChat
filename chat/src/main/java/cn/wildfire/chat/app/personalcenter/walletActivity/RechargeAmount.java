package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.alipay.AliPayHandler;
import cn.wildfire.chat.app.alipay.AliPayUtils;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.adapter.RechargeMoneyAdapter;
import cn.wildfire.chat.app.personalcenter.utils.ProgressDialogUtil;
import cn.wildfire.chat.app.shop.adapter.PayAdapter;
import cn.wildfire.chat.app.shop.utils.GridSpacingItemDecoration;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 充值
 */
public class RechargeAmount extends BaseActivity {

    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.recharge_list)
    RecyclerView rechargeList;
    @BindView(R.id.pay_list)
    RecyclerView payList;
    @BindView(R.id.next)
    RadiusTextView next;
    private int mPosition = 0;
    private AliPayHandler aliPayHandler;

    private RechargeMoneyAdapter rechargeMoneyAdapter;

    @Override
    protected int contentLayout() {
        return R.layout.activity_recharge_amount;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("充值");
        aliPayHandler = new AliPayHandler() {
            @Override
            protected void onSuccess() {
//                ToastUtils.show("充值成功");
            }

            @Override
            protected void onChecking() {

            }

            @Override
            protected void onFail() {
                ToastUtils.show("充值失败");
            }

        };


        rechargeList.setLayoutManager(new GridLayoutManager(this, 3));
        rechargeList.addItemDecoration(new GridSpacingItemDecoration(3, UIUtils.dip2Px(10), true));
        payList.setLayoutManager(new LinearLayoutManager(RechargeAmount.this));
        payList.addItemDecoration(new ItemDecorationDivider(RechargeAmount.this,
                ItemDecorationDivider.VERTICAL_LIST, 1,
                ContextCompat.getColor(RechargeAmount.this, R.color.line)));
        List<String> list = new ArrayList<>();
        list.add("10");
        list.add("50");
        list.add("100");
        list.add("200");
        list.add("300");
        list.add("500");
        rechargeMoneyAdapter = new RechargeMoneyAdapter(R.layout.item_recharge, list);
        rechargeList.setAdapter(rechargeMoneyAdapter);
        rechargeMoneyAdapter.setOnItemClickListener((adapter, view, position) -> {
            rechargeMoneyAdapter.setmPosition(position);
            money.setText(list.get(position));
            money.setSelection(list.get(position).length());
            money.requestFocus();
        });
        final PayAdapter payAdapter = new PayAdapter(R.layout.item_pay, Util.getPay());
        payList.setAdapter(payAdapter);
        payAdapter.setOnItemClickListener((adapter, view, position) -> {
            mPosition = position;
            payAdapter.setPosition(position);
        });

    }

    @OnClick(R.id.next)
    public void onViewClicked() {
        //充值  支付宝或微信
        if (TextUtils.isEmpty(money.getText().toString().trim())) {
            ToastUtils.show("请选择或输入要充值的金额");
            return;
        }
        if (mPosition == -1) {
            ToastUtils.show("支付宝支付未选择");
            return;
        }

        onRecharge(money.getText().toString().trim(), "1");
    }

    /**
     * 去支付
     *
     * @param money
     * @param type
     */
    private void onRecharge(String money, String type) {
        ProgressDialogUtil.showLoading(this, "正在提交充值请求...");

        ApiMethodFactory.getInstance().onRecharge(money, ChatManager.Instance().getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    onPay(type, obj.getData());
                }
//                ToastUtils.show(obj.getMessage());
                ProgressDialogUtil.dismiss(RechargeAmount.this);
            }
        });
    }

    /**
     * 充值
     *
     * @param type
     * @param orderId
     */
    private void onPay(String type, String orderId) {
        ApiMethodFactory.getInstance().onPayOnWallet(orderId, type, ChatManager.Instance().getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    AliPayUtils aliPayUtils = new AliPayUtils(RechargeAmount.this, aliPayHandler);
                    aliPayUtils.pay(obj.getData());
                }
            }
        });
    }
}
