package cn.wildfire.chat.app.personalcenter.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.allen.library.SuperTextView;
import com.hjq.toast.ToastUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.main.SplashActivity;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcWebViewActivity;
import cn.wildfire.chat.kit.conversationlist.ConversationListViewModel;
import cn.wildfire.chat.kit.conversationlist.ConversationListViewModelFactory;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.ConversationInfo;
import cn.wildfirechat.remote.ChatManager;

public class Setting extends BaseActivity {

    @BindView(R.id.agree)
    SuperTextView agree;
    @BindView(R.id.add_type)
    SuperTextView addType;
    @BindView(R.id.yinsi)
    SuperTextView yinsi;
    @BindView(R.id.login_pwd)
    SuperTextView loginPwd;
    @BindView(R.id.hezuo)
    SuperTextView hezuo;
    @BindView(R.id.about)
    SuperTextView about;
    @BindView(R.id.clear)
    SuperTextView clear;
    @BindView(R.id.clear_message)
    SuperTextView clearMessage;
    @BindView(R.id.exit)
    SuperTextView exit;

    @Override
    protected int contentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("通用");

        about.setRightString(Util.getVersionName(this));
    }

    @OnClick({R.id.agree, R.id.add_type, R.id.yinsi, R.id.login_pwd, R.id.about, R.id.clear, R.id.clear_message, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.agree:
                //萌聊用户使用协议
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "http://api.aawlkj.xyz/xiey.html");
                intent.putExtra("title", "萌聊用户使用协议");
                startActivity(intent);
                break;
            case R.id.add_type:
                //添加我的方式
                startActivity(new Intent(this, AddTypeFriend.class));
                break;
            case R.id.yinsi:
                //萌聊隐私政策
                Intent intent2 = new Intent(this, WebViewActivity.class);
                intent2.putExtra("url", "http://api.aawlkj.xyz/yins.html");
                intent2.putExtra("title", "萌聊隐私政策");
                startActivity(intent2);
                break;
            case R.id.login_pwd:
                //登录密码
                Intent intent3 = new Intent(this, SetPwd.class);
                startActivity(intent3);
                break;
            case R.id.about:
                //关于
                break;
            case R.id.clear:
                //清除缓存
                ToastUtils.show("清除成功");
                break;
            case R.id.clear_message:
                //清除聊天记录
                ConversationListViewModel conversationListViewModel = ViewModelProviders
                        .of(this, new ConversationListViewModelFactory(Arrays.asList(Conversation.ConversationType.Single, Conversation.ConversationType.Group), Arrays.asList(0)))
                        .get(ConversationListViewModel.class);
                List<Conversation.ConversationType> types = Arrays.asList(Conversation.ConversationType.Single,
                        Conversation.ConversationType.Group);
                List<Integer> liens = Arrays.asList(0);
                List<ConversationInfo> conversationInfos = conversationListViewModel.getConversationList(types, liens);
                for (int i = 0; i < conversationInfos.size(); i++) {
                    ChatManager.Instance().clearMessages(conversationInfos.get(i).conversation);
                }
                break;
            case R.id.exit:
                //退出
                exit();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void exit() {
        ChatManagerHolder.gChatManager.disconnect(true, true);
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
