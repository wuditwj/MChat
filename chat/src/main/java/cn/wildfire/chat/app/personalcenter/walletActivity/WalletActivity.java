package cn.wildfire.chat.app.personalcenter.walletActivity;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.SuperTextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.chat.R;

/**
 * 我的余额
 */
public class WalletActivity extends BaseActivity {

    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.recharge)
    RelativeLayout recharge;
    @BindView(R.id.with_draw)
    RelativeLayout withDraw;
    @BindView(R.id.title_1)
    LinearLayout title1;
    @BindView(R.id.bill)
    SuperTextView bill;
    @BindView(R.id.red_package_recode)
    SuperTextView redPackageRecode;
    @BindView(R.id.recharge_record)
    SuperTextView rechargeRecord;
    @BindView(R.id.pay_pwd)
    SuperTextView payPwd;
    @BindView(R.id.ali)
    SuperTextView ali;
    @BindView(R.id.bank)
    SuperTextView bank;

    @Override
    protected int contentLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("我的余额");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAmount();
    }

    private void getAmount() {
        ApiMethodFactory.getInstance().getAmount(ChatManagerHolder.gChatManager.getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<Double> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<Double>>() {
                });
                DecimalFormat numberFormat = new DecimalFormat("￥#######0.00");
                amount.setText(numberFormat.format(obj.getData()));
            }
        });
    }

    @OnClick({R.id.recharge, R.id.with_draw, R.id.bill, R.id.red_package_recode, R.id.recharge_record, R.id.pay_pwd, R.id.ali, R.id.bank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge:
                //充值
                startActivity(new Intent(this, RechargeAmount.class));
                break;
            case R.id.with_draw:
                //提现
                startActivity(new Intent(this,WithDrawActivity.class));
                break;
            case R.id.bill:
                //账单
                startActivity(new Intent(this, BillActivity.class));
                break;
            case R.id.red_package_recode:
                //红包记录
                startActivity(new Intent(this, ReceiveAndSendPackageList.class));
                break;
            case R.id.recharge_record:
                //充值记录
                break;
            case R.id.pay_pwd:
                //修改支付密码
                startActivity(new Intent(this,WalletPayCode.class));
                break;
            case R.id.ali:
                //提现到支付宝
                break;
            case R.id.bank:
                //我的银行卡
                startActivity(new Intent(this, BankListActivity.class));
                break;
        }
    }
}
