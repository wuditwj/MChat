package cn.wildfire.chat.app.envelope;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.SuperButton;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.app.ConfigPath;
import cn.wildfire.chat.app.entity.BaseEntity;
import cn.wildfire.chat.app.entity.BasicEntity;
import cn.wildfire.chat.app.entity.RedEnvelopeEntity;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.personalcenter.walletActivity.WalletCode;
import cn.wildfire.chat.app.util.EditTextUtils;
import cn.wildfire.chat.app.widget.PayPwdView;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.conversation.ConversationViewModel;
import cn.wildfire.chat.kit.viewmodel.MessageViewModel;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class RedPackageActivity extends WfcBaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.editor_money)
    EditText editorMoney;
    @BindView(R.id.num)
    EditText num;
    @BindView(R.id.package_number)
    LinearLayout packageNumber;
    @BindView(R.id.choose_user)
    TextView chooseUser;
    @BindView(R.id.package_get)
    LinearLayout packageGet;
    @BindView(R.id.member_number)
    TextView memberNumber;
    @BindView(R.id.envelope_txt)
    EditText envelopeTxt;
    @BindView(R.id.envelope_money)
    TextView envelopeMoney;
    @BindView(R.id.submit)
    SuperButton submit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.left_menu)
    TextView leftMenu;
    private Conversation conversation;
    private String type;
    private String redNum = "1";
    private String sendType = "1";
    private String toUser;
    private GroupInfo groupInfo;
    private String redPackageTitle = "";
    private String nickName;

    protected MessageViewModel messageViewModel;
    private boolean isDialog;

    @Override
    protected int contentLayout() {
        return R.layout.activity_red_package;
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        title.setText("发红包");
        conversation = getIntent().getParcelableExtra("conversation");
        String userId = conversation.target;
        groupInfo = ChatManager.Instance().getGroupInfo(userId, true);
        type = getIntent().getStringExtra("type");//3 专属红包

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        isDialog = getIntent().getBooleanExtra("isDialog", false);
        UserInfo userInfoIsDialog = getIntent().getParcelableExtra("userInfo");

        toolbar.setNavigationIcon(null);
        leftMenu.setText("取消");
        UserInfo userInfo = ChatManager.Instance().getUserInfo(conversation.target, true);
        toUser = userInfo.uid;
        nickName = userInfo.displayName;
        //从dialog进来,指定红包
        if (userInfoIsDialog != null) {
            chooseUser.setText(userInfoIsDialog.displayName);
            toUser = userInfoIsDialog.uid;
            nickName = userInfoIsDialog.displayName;
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (conversation.type == Conversation.ConversationType.Group) {
            //群聊
            if (TextUtils.equals(type, "3")) {
                //专属红包
                packageNumber.setVisibility(View.GONE);//不显示红包数量
                redNum = "1";
                sendType = "3";
                packageGet.setVisibility(View.VISIBLE);//选择某人
            } else {
                num.setHint("1");
                num.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        redNum = s.toString();
                    }
                });
                //普通群红包
                sendType = "2";
            }
        } else if (conversation.type == Conversation.ConversationType.Single) {
            //普通单独红包
            redNum = "1";
            sendType = "1";
            packageNumber.setVisibility(View.GONE);//不显示红包数量
        }
        EditTextUtils.afterDotTwo(editorMoney, envelopeMoney);

    }


    @OnClick({R.id.left_menu, R.id.choose_user, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                onBackPressed();
                break;
            case R.id.choose_user:
                ARouter.getInstance()
                        .build(ConfigPath.GroupMember)
                        .withInt("flag", 0)
                        .withString("groupId", groupInfo.target)
                        .navigation(this, 1000);
                break;
            case R.id.submit:
                checkPwd();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1000) {
            UserInfo userInfo = data.getParcelableExtra("userInfo");
            toUser = userInfo.uid;
            chooseUser.setText(userInfo.displayName);
            nickName = userInfo.displayName;
        }


    }

    private void checkPwd() {
        ApiMethodFactory.getInstance().CheckPayPwd(new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BasicEntity<String> baseBean = JSONObject.parseObject(response, new TypeReference<BasicEntity<String>>() {
                });
                if (baseBean.getCode() == 200) {
                    SendRedPackage();
                } else {
                    //设置支付密码
                    //为了您的账户安全,请先设置支付密码
                    startActivity(new Intent(RedPackageActivity.this, WalletCode.class));

                }
            }
        });
    }

    private void SendRedPackage() {
        if (TextUtils.isEmpty(envelopeTxt.getText().toString().trim())) {
            redPackageTitle = "恭喜发财,大吉大利";
        } else {
            redPackageTitle = envelopeTxt.getText().toString().trim();
        }
        if (TextUtils.equals("2", sendType)) {
            //普通群发 toUser 设置群号id;
            toUser = groupInfo.target;
        }
        if (TextUtils.isEmpty(editorMoney.getText().toString().trim())) {
            ToastUtils.show("请输入金额");
            return;
        }
        Double _double = Double.parseDouble(editorMoney.getText().toString().trim());
        PayFragment payFragment = PayFragment.newInstance(String.format("%.2f", _double));
        payFragment.show(getSupportFragmentManager(), "Pay");
        payFragment.setPaySuccessCallBack(new PayPwdView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                ApiMethodFactory.getInstance().onSendRedPackage(ChatManagerHolder.gChatManager.getUserId(),
                        toUser, editorMoney.getText().toString().trim(), redNum, redPackageTitle, sendType,
                        result, new HttpHandler() {
                            @Override
                            public void requestSuccess(String response) {
                                BasicEntity<RedEnvelopeEntity> baseBean = JSONObject.parseObject(response, new TypeReference<BasicEntity<RedEnvelopeEntity>>() {
                                });
                                if (baseBean.getCode() == 200) {
                                    Intent data = new Intent();
                                    baseBean.getData().setNickName(nickName);
                                    data.putExtra("redEnvelopeVo", baseBean.getData());
                                    setResult(Activity.RESULT_OK, data);
                                    //判断是否从dialog进来的
                                    if (isDialog) {
                                        //
                                        messageViewModel.sendRedPackage(conversation, baseBean.getData());
                                    }
                                    finish();
                                } else {
                                    ToastUtils.show(baseBean.getMessage());
                                }
                            }
                        });
            }
        });

    }
}
