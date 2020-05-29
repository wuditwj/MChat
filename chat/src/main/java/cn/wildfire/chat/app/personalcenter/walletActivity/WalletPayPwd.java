package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.widget.PassWordLayout;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 设置支付密码
 */
public class WalletPayPwd extends BaseActivity {

    @BindView(R.id.passLayout1)
    PassWordLayout passLayout1;
    @BindView(R.id.passLayout2)
    PassWordLayout passLayout2;

    @Override
    protected int contentLayout() {
        return R.layout.activity_wallet_pay_pwd;
    }

    @Override
    protected void init() {
        //设置支付密码
        passLayout2.setPwdChangeListener(new PassWordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {

            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String pwd) {
                setAccount(pwd);
            }
        });
    }

    /**
     * 设置密码
     *
     * @param pwd
     */
    private void setAccount(String pwd) {
        ApiMethodFactory.getInstance().setAccount(ChatManager.Instance().getUserId(), passLayout1.getPassString(), pwd, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> baseBean = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (baseBean.getCode() == 200) {
                    finish();
                }
                ToastUtils.show(baseBean.getMessage());
            }
        });
    }

}
