package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.utils.UtilCountDownTimer;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

/**
 * 重设支付密码
 */
public class WalletPayCode extends BaseActivity {

    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.get_code)
    RadiusTextView getCode;
    @BindView(R.id.next)
    RadiusTextView next;

    private UtilCountDownTimer mc;
    private UserInfo userInfo;

    @Override
    protected int contentLayout() {
        return R.layout.activity_wallet_pay_code;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("重设支付密码");
        userInfo = ChatManager.Instance().getUserInfo(ChatManager.Instance().getUserId(), true);
        String ss = userInfo.mobile.substring(0, userInfo.mobile.length() - (userInfo.mobile.substring(3)).length()) + "****" + userInfo.mobile.substring(7);
        mobile.setText("请输入手机号" + ss + "收到的短信验证码");
        mc = new UtilCountDownTimer(getCode, 60000, 1000);
    }

    @OnClick({R.id.get_code, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_code:
                getCode();
                break;
            case R.id.next:
                next();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        ApiMethodFactory.getInstance().getCode(userInfo.mobile, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    mc.start();
                }
                ToastUtils.show(obj.getMessage());
            }
        });
    }

    /**
     * 下一步
     */
    private void next() {
        ApiMethodFactory.getInstance().next(userInfo.mobile, code.getText().toString().trim(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    startActivity(new Intent(WalletPayCode.this, WalletPayPwd.class));
                    finish();
                }
                ToastUtils.show(obj.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mc != null) mc.cancel();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mc != null) mc.cancel();
    }
}
