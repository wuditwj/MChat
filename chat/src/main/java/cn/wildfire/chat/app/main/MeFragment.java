package cn.wildfire.chat.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.SuperTextView;
import com.aries.ui.view.radius.RadiusLinearLayout;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.gyf.barlibrary.ImmersionBar;
import com.king.zxing.Intents;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.personalcenter.FeedBackActivity;
import cn.wildfire.chat.app.personalcenter.NewMessageActivity;
import cn.wildfire.chat.app.personalcenter.ReportReasonListActivity;
import cn.wildfire.chat.app.personalcenter.personalInformationActivity.UserInfoActivity;
import cn.wildfire.chat.app.personalcenter.setting.Setting;
import cn.wildfire.chat.app.personalcenter.walletActivity.WalletActivity;
import cn.wildfire.chat.app.personalcenter.walletActivity.WalletCode;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.channel.ChannelInfoActivity;
import cn.wildfire.chat.kit.group.GroupInfoActivity;
import cn.wildfire.chat.kit.qrcode.ScanQRCodeActivity;
import cn.wildfire.chat.kit.settings.blacklist.BlacklistListActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {

    @BindView(R.id.toolBar_top)
    View toolBarTop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.sex)
    ImageView sex;
    @BindView(R.id.accountTextView)
    TextView accountTextView;
    @BindView(R.id.meLinearLayout)
    RadiusLinearLayout meLinearLayout;
    @BindView(R.id.wallet)
    SuperTextView wallet;
    @BindView(R.id.qr_sao)
    SuperTextView qrSao;
    @BindView(R.id.setting)
    SuperTextView setting;
    @BindView(R.id.feed_back)
    SuperTextView feedBack;
    @BindView(R.id.new_message)
    SuperTextView newMessage;
    @BindView(R.id.safe)
    SuperTextView safe;
    @BindView(R.id.report)
    SuperTextView report;
    @BindView(R.id.help)
    SuperTextView help;
    @BindView(R.id.blackList)
    SuperTextView blackList;

    private UserViewModel userViewModel;
    private UserInfo userInfo;

    private static final int REQUEST_CODE_SCAN_QR_CODE = 100;
    private Observer<List<UserInfo>> userInfoLiveDataObserver = new Observer<List<UserInfo>>() {
        @Override
        public void onChanged(@Nullable List<UserInfo> userInfos) {
            if (userInfos == null) {
                return;
            }
            for (UserInfo info : userInfos) {
                if (info.uid.equals(userViewModel.getUserId())) {
                    userInfo = info;
                    updateUserInfo(userInfo);
                    break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }


    private void init() {
        ImmersionBar.with(getActivity()).statusBarView(toolBarTop).init();
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUserInfoAsync(userViewModel.getUserId(), true)
                .observe(this, info -> {
                    userInfo = info;
                    if (userInfo != null) {
                        updateUserInfo(userInfo);
                    }
                });
        userViewModel.userInfoLiveData().observeForever(userInfoLiveDataObserver);

    }

    /**
     * 显示个人信息
     *
     * @param userInfo
     */
    private void updateUserInfo(UserInfo userInfo) {
        GlideApp
                .with(this)
                .load(userInfo.portrait)
                .placeholder(R.mipmap.avatar_def)
                .transforms(new CenterCrop(), new RoundedCorners(10))
                .into(portraitImageView);
        nameTextView.setText(userInfo.displayName);
        if (TextUtils.isEmpty(userInfo.email)) {
            accountTextView.setText("萌聊号:未设置");
        } else {
            accountTextView.setText("萌聊号:" + userInfo.email);
        }

        if (userInfo.gender == 1) {
            GlideApp.with(this).load(R.mipmap.ic_boy).into(sex);
        } else if (userInfo.gender == 2) {
            GlideApp.with(this).load(R.mipmap.ic_girl).into(sex);
        } else {
            sex.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.meLinearLayout, R.id.wallet, R.id.qr_sao, R.id.setting, R.id.feed_back, R.id.new_message, R.id.report, R.id.blackList})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.meLinearLayout:
                //个人布局
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("userInfo", userInfo);
                startActivity(intent);
                break;
            case R.id.wallet:
                //钱包
                setWallet();
                break;
            case R.id.qr_sao:
                //扫一扫
                startActivityForResult(new Intent(getActivity(), ScanQRCodeActivity.class), REQUEST_CODE_SCAN_QR_CODE);
                break;
            case R.id.setting:
                //通用
                startActivity(new Intent(getActivity(), Setting.class));
                break;
            case R.id.feed_back:
                //意见反馈
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.new_message:
                //新消息通知
                startActivity(new Intent(getActivity(), NewMessageActivity.class));
                break;
            case R.id.report:
                //举报与投诉
                startActivity(new Intent(getActivity(), ReportReasonListActivity.class));
                break;
            case R.id.blackList:
                //黑名单
                startActivity(new Intent(getActivity(), BlacklistListActivity.class));
                break;
        }
    }

    /**
     * 钱包
     */
    private void setWallet() {
        ApiMethodFactory.getInstance().isPayPwd(ChatManager.Instance().getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> baseBean = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (baseBean.getCode() == 200) {
                    startActivity(new Intent(getActivity(), WalletActivity.class));
                } else {
                    //设置支付密码
                    //为了您的账户安全,请先设置支付密码
                    startActivity(new Intent(getActivity(), WalletCode.class));

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userViewModel.userInfoLiveData().removeObserver(userInfoLiveDataObserver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCAN_QR_CODE:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    onScanPcQrCode(result);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void onScanPcQrCode(String qrcode) {
        String prefix = qrcode.substring(0, qrcode.lastIndexOf('/') + 1);
        String value = qrcode.substring(qrcode.lastIndexOf("/") + 1);
        switch (prefix) {
            case WfcScheme.QR_CODE_PREFIX_PC_SESSION:
                pcLogin(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_USER:
                showUser(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_GROUP:
                joinGroup(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_CHANNEL:
                subscribeChannel(value);
                break;
            default:
                Toast.makeText(getActivity(), "qrcode: " + qrcode, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void subscribeChannel(String channelId) {
        Intent intent = new Intent(getActivity(), ChannelInfoActivity.class);
        intent.putExtra("channelId", channelId);
        startActivity(intent);
    }

    private void joinGroup(String groupId) {
        Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    private void showUser(String uid) {

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        UserInfo userInfo = userViewModel.getUserInfo(uid, true);
        if (userInfo == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    private void pcLogin(String token) {
        Intent intent = new Intent(getActivity(), PCLoginActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
