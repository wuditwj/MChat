package cn.wildfire.chat.app.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.MainActivity;
import cn.wildfire.chat.app.entity.BasicEntity;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.util.UtilCountDownTimer;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.WfcWebViewActivity;
import cn.wildfirechat.chat.R;

public class RegisterActivity extends WfcBaseActivity {
    @BindView(R.id.phoneNumberEditText)
    EditText phoneNumberEditText;
    @BindView(R.id.authCodeEditText)
    EditText authCodeEditText;
    @BindView(R.id.requestAuthCodeButton)
    Button requestAuthCodeButton;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.loginButton)
    RadiusTextView loginButton;
    @BindView(R.id.xieyi)
    TextView xieyi;
    @BindView(R.id.yinsi)
    TextView yinsi;
    private UtilCountDownTimer mc;

    @Override
    protected int contentLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        mc = new UtilCountDownTimer(requestAuthCodeButton, 60000, 1000);
    }

    @OnClick(R.id.xieyi)
    void xieyi() {
        Intent intent = new Intent(this, WfcWebViewActivity.class);
        intent.putExtra("title", "萌聊IM用户协议");
        intent.putExtra("url", "http://api.aawlkj.xyz/xiey.html");

        startActivity(intent);
    }

    @OnClick(R.id.yinsi)
    void yinsi() {
        Intent intent = new Intent(this, WfcWebViewActivity.class);
        intent.putExtra("title", "萌聊IM隐私政策");
        intent.putExtra("url", "http://api.aawlkj.xyz/yins.html");
        startActivity(intent);
    }

    @OnClick(R.id.loginButton)
    void login() {
        ApiMethodFactory.getInstance().onRegister(phoneNumberEditText.getText().toString().trim(), password.getText().toString().trim(),
                authCodeEditText.getText().toString().trim(), response -> {
                    BasicEntity<String> obj = JSONObject.parseObject(response, new TypeReference<BasicEntity<String>>() {
                    });
                    if (obj.getCode() == 0) {
                        JSONObject json = JSON.parseObject(response);
                        JSONObject result = JSONObject.parseObject(json.getString("result"));

                        ChatManagerHolder.gChatManager.connect(result.getString("userId"),
                                result.getString("token"));
                        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                        sp.edit()
                                .putString("id", result.getString("userId"))
                                .putString("token", result.getString("token"))
                                .apply();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(obj.getMessage());
                    }
                });
    }

    @OnClick(R.id.requestAuthCodeButton)
    void requestAuthCode() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        ApiMethodFactory.getInstance().getCode(phoneNumber, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BasicEntity<String> obj = JSONObject.parseObject(response, new TypeReference<BasicEntity<String>>() {
                });
                if (obj.getCode() == 200) {
                    mc.start();
                }
                ToastUtils.show(obj.getMessage());
            }
        });


    }

}
