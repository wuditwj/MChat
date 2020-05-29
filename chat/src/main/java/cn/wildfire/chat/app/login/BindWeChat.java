package cn.wildfire.chat.app.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.entity.BasicEntity;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.MainActivity;
import cn.wildfire.chat.app.util.UtilCountDownTimer;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.chat.R;

public class BindWeChat extends WfcBaseActivity {
    @BindView(R.id.phoneNumberEditText)
    EditText phoneNumberEditText;
    @BindView(R.id.authCodeEditText)
    EditText authCodeEditText;
    @BindView(R.id.requestAuthCodeButton)
    Button requestAuthCodeButton;
    @BindView(R.id.loginButton)
    Button loginButton;
    private UtilCountDownTimer mc;
    private String openId;
    private String disName;
    private String pic;


    @Override
    protected int contentLayout() {
        return R.layout.activity_bind_chat;
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        openId = getIntent().getStringExtra("openId");
        disName = getIntent().getStringExtra("nickName");
        pic = getIntent().getStringExtra("face");
        mc = new UtilCountDownTimer(requestAuthCodeButton,60000, 1000);


    }

    @OnClick({R.id.requestAuthCodeButton, R.id.loginButton})
    public void onViewClicked(View view) {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        switch (view.getId()) {
            case R.id.requestAuthCodeButton:
                ApiMethodFactory.getInstance().getCode(phoneNumber, new HttpHandler() {
                    @Override
                    public void requestSuccess(String response) {
                        BasicEntity<String> obj= JSONObject.parseObject(response,new TypeReference<BasicEntity<String>>(){});
                        if (obj.getCode()==200){
                            mc.start();
                        }
                        ToastUtils.show(obj.getMessage());
                    }
                });

                break;
            case R.id.loginButton:
                ld.show();
                ld.setLoadingText("正在绑定微信...");
                ApiMethodFactory.getInstance().onBindWeChat(openId, disName, pic, phoneNumber, authCodeEditText.getText().toString().trim(), new HttpHandler() {
                    @Override
                    public void requestSuccess(String response) {
                        JSONObject json = JSON.parseObject(response);
                        JSONObject result = JSONObject.parseObject(json.getString("result"));

                        ChatManagerHolder.gChatManager.connect(result.getString("userId"),
                                result.getString("token"));
                        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                        sp.edit()
                                .putString("id", result.getString("userId"))
                                .putString("token", result.getString("token"))
                                .apply();
                        Intent intent = new Intent(BindWeChat.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        dismissContentLoading();
                    }
                });
                break;
        }
    }
}
