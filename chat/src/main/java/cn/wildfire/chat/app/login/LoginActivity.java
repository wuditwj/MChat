package cn.wildfire.chat.app.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.SuperButton;
import com.aries.ui.view.radius.RadiusLinearLayout;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.wildfire.chat.app.AppService;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.ConfigPath;
import cn.wildfire.chat.app.MainActivity;
import cn.wildfire.chat.app.entity.BaseEntity;
import cn.wildfire.chat.app.entity.BasicEntity;
import cn.wildfire.chat.app.entity.LoginEntity;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.login.model.LoginResult;
import cn.wildfire.chat.app.util.UtilCountDownTimer;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.WfcWebViewActivity;
import cn.wildfirechat.chat.R;

/**
 * num:130 0000 0001
 * pwd:123456
 */
@Route(path = ConfigPath.LoginPath)
public class LoginActivity extends WfcBaseActivity implements PlatformActionListener, Handler.Callback {

    @BindView(R.id.accountEditText)
    EditText accountEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.login)
    SuperButton login;
    @BindView(R.id.register)
    SuperButton register;
    @BindView(R.id.pwd_login)
    CheckBox pwdLogin;
    @BindView(R.id.weChat)
    ImageView weChat;
    @BindView(R.id.xieyi)
    TextView xieyi;
    @BindView(R.id.yinsi)
    TextView yinsi;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.authCodeEditText)
    EditText authCodeEditText;
    @BindView(R.id.requestAuthCodeButton)
    Button requestAuthCodeButton;
    @BindView(R.id.login_sms_layout)
    RadiusLinearLayout loginSmsLayout;
    @BindView(R.id.login_pwd_layout)
    LinearLayout loginPwdLayout;
    private boolean isLoginForPwd = false;
    private UtilCountDownTimer mc;
    private Platform platform;
    private Handler handler;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;

    @Override
    protected int contentLayout() {
        return R.layout.login_activity_account;
    }

    @Override
    protected boolean showHomeMenuItem() {
        return false;
    }

//    @OnTextChanged(value = R.id.accountEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    void inputAccount(Editable editable) {
//        if (!TextUtils.isEmpty(passwordEditText.getText()) && !TextUtils.isEmpty(editable)) {
//            login.setEnabled(true);
//        } else {
//            login.setEnabled(false);
//        }
//    }

    @Override
    protected void afterViews() {
        super.afterViews();
        toolbar.setBackgroundColor(Color.WHITE);
        handler = new Handler(this);
        mc = new UtilCountDownTimer(requestAuthCodeButton, 60000, 1000);

        pwdLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLoginForPwd = isChecked;
                pwdLogin.setText(isChecked ? "账号密码登录" : "手机验证码登录");
                if (isChecked) {
                    loginPwdLayout.setVisibility(View.GONE);
                    loginSmsLayout.setVisibility(View.VISIBLE);
                } else {
                    loginPwdLayout.setVisibility(View.VISIBLE);
                    loginSmsLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnTextChanged(value = R.id.passwordEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void inputPassword(Editable editable) {
        if (!TextUtils.isEmpty(accountEditText.getText()) && !TextUtils.isEmpty(editable)) {
            login.setEnabled(true);
        } else {
            login.setEnabled(false);
        }
    }


    @OnClick({R.id.login, R.id.register, R.id.pwd_login, R.id.weChat, R.id.xieyi, R.id.yinsi, R.id.requestAuthCodeButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                ld.show();
                ld.setLoadingText("登录中...");

                if (isLoginForPwd) {//验证码登录
                    String phoneNumber = accountEditText.getText().toString().trim();
                    String authCode = authCodeEditText.getText().toString().trim();
                    AppService.Instance().smsLogin(phoneNumber, authCode, new AppService.LoginCallback() {
                        @Override
                        public void onUiSuccess(LoginResult loginResult) {
                            dismissContentLoading();
                            if (isFinishing()) {
                                return;
                            }
                            //需要注意token跟clientId是强依赖的，一定要调用getClientId获取到clientId，然后用这个clientId获取token，这样connect才能成功，如果随便使用一个clientId获取到的token将无法链接成功。
                            ChatManagerHolder.gChatManager.connect(loginResult.getUserId(), loginResult.getToken());
                            SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                            sp.edit()
                                    .putString("id", loginResult.getUserId())
                                    .putString("token", loginResult.getToken())
                                    .apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onUiFailure(int code, String msg) {
                            if (isFinishing()) {
                                return;
                            }
                            dismissContentLoading();
                            ToastUtils.show("登录失败：" + code + " " + msg);
                        }
                    });
                } else {
                    ApiMethodFactory.getInstance().onLogin(accountEditText.getText().toString().trim(),
                            passwordEditText.getText().toString().trim(), new HttpHandler() {
                                @Override
                                public void requestSuccess(String response) {
                                    BaseEntity<LoginEntity> obj = JSONObject.parseObject(response, new TypeReference<BaseEntity<LoginEntity>>() {
                                    });
                                    dismissContentLoading();
                                    if (obj.getCode() == 0) {
                                        ChatManagerHolder.gChatManager.connect(obj.getResult().getUserId(),
                                                obj.getResult().getToken());
                                        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                                        sp.edit()
                                                .putString("id", obj.getResult().getUserId())
                                                .putString("token", obj.getResult().getToken())
                                                .apply();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        ToastUtils.show(obj.getMessage());
                                    }
                                }
                            });
                }

                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));

                break;
            case R.id.weChat:
                platform = ShareSDK.getPlatform(Wechat.NAME);
                authorize(platform);
                platform.removeAccount(true);
                break;
            case R.id.xieyi:
                Intent intent = new Intent(this, WfcWebViewActivity.class);
                intent.putExtra("title", "萌聊IM用户协议");
                intent.putExtra("url", "http://api.aawlkj.xyz/xiey.html");

                startActivity(intent);
                break;
            case R.id.yinsi:
                Intent intent1 = new Intent(this, WfcWebViewActivity.class);
                intent1.putExtra("title", "萌聊IM隐私政策");
                intent1.putExtra("url", "http://api.aawlkj.xyz/yins.html");
                startActivity(intent1);

                break;
            case R.id.requestAuthCodeButton:
                //验证码
                ApiMethodFactory.getInstance().requestAuthCode(accountEditText.getText().toString().trim(), new HttpHandler() {
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
                break;
        }
    }

    private void authorize(Platform plat) {
        ld.show();
        ld.setLoadingText("正在登录...");
        if (plat == null) {
            return;
        }
        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                //取消授权
                ToastUtils.show("授权操作已取消");
                dismissContentLoading();
            }
            break;
            case MSG_AUTH_ERROR: {
                //授权失败
                ToastUtils.show("需要下载微信端");
                dismissContentLoading();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                if (platform != null) {
                    //登录  拉去授权成功
                    ApiMethodFactory.getInstance().onWeChatLogin(platform.getDb().getUserId(), new HttpHandler() {
                        @Override
                        public void requestSuccess(String response) {
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
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else if (obj.getCode() == 11) {
                                Intent intent = new Intent(LoginActivity.this, BindWeChat.class);
                                intent.putExtra("openId", platform.getDb().getUserId());
                                intent.putExtra("face", platform.getDb().getUserIcon());
                                intent.putExtra("nickName", platform.getDb().getUserName());
                                startActivity(intent);
                            }
                            dismissContentLoading();
                        }
                    });
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (i == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), hashMap};
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }
}
