package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.SuperTextView;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.entity.CardVo;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.chat.R;

/**
 * 提现
 */
public class WithDrawActivity extends BaseActivity {

    @BindView(R.id.choose_bank)
    SuperTextView chooseBank;
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.amount)
    SuperTextView amount;
    @BindView(R.id.money_account)
    SuperTextView moneyAccount;
    @BindView(R.id.submit)
    RadiusTextView submit;

    private String account = "";

    @Override
    protected int contentLayout() {
        return R.layout.activity_with_draw;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("提现");

        getAmount();

        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (Double.parseDouble(s.toString()) > 10000) {
                        s = "10000";
                        money.setText(s);
                        money.setSelection(s.length());
                        double am = 10000 * 0.006 + 1;
                        BigDecimal bg = new BigDecimal(am);
                        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        amount.setLeftString("服务费:" + f1);
                        moneyAccount.setRightString("" + (10000 - f1));
                    } else if (Double.parseDouble(s.toString()) < 10) {
                        moneyAccount.setRightString("0");
                    } else {
                        double am = Double.parseDouble(s.toString()) * 0.006 + 1;
                        BigDecimal bg = new BigDecimal(am);
                        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        amount.setLeftString("服务费:" + f1);
                        moneyAccount.setRightString("" + (Double.parseDouble(s.toString()) - f1));
                    }

                } else {
                    getAmount();
                    moneyAccount.setRightString("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @BusReceiver
    public void onMainThread(CardVo event) {
        account = event.getBank_account();
        chooseBank.setRightString(event.getBank_account());
    }

    /**
     * 获取当前余额
     */
    private void getAmount() {
        ApiMethodFactory.getInstance().getAmount(ChatManagerHolder.gChatManager.getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<Double> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<Double>>() {
                });
                amount.setLeftString("当前余额:" + Util.decimalFormatMoney(obj.getData()));
                amount.getCenterTextView().setOnClickListener(v -> {
                    String amount = obj.getData() + "";
                    money.setText(amount);
                    Log.i("--==>>", money.length() + "");
                    money.setSelection(money.length());
                    money.requestFocus();
                });
            }
        });
    }

    @OnClick({R.id.choose_bank, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_bank:
                //选择银行卡
                Intent intent = new Intent(this, BankListActivity.class);
                intent.putExtra("choose", true);
                startActivity(intent);
                break;
            case R.id.submit:
                //确认提现
                if (TextUtils.isEmpty(account)) {
                    ToastUtils.show("请选择提现银行卡");
                    return;
                }
                if (TextUtils.isEmpty(money.getText().toString().trim())) {
                    ToastUtils.show("请输入提现金额");
                    return;
                }
                ApiMethodFactory.getInstance().withDraw(ChatManagerHolder.gChatManager.getUserId(),
                        money.getText().toString().trim(),
                        account,
                        moneyAccount.getRightString(),
                        new HttpHandler() {
                            @Override
                            public void requestSuccess(String response) {
                                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                                });
                                if (obj.getCode() == 200) {
                                    ToastUtils.show("提现申请成功，预计24小时内提现到账");
                                    finish();
                                }
                                ToastUtils.show(obj.getMessage());
                            }
                        });
                break;
        }
    }
}
