package cn.wildfire.chat.kit.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.allen.library.CircleImageView;
import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.kyleduo.switchbutton.SwitchButton;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import cn.wildfire.chat.app.BlackUserList;
//import cn.wildfire.chat.app.FriendListActivity;
import cn.wildfire.chat.app.MainActivity;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.ContactListActivity;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.contact.newfriend.InviteFriendActivity;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfire.chat.kit.qrcode.QRCodeActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfire.chat.kit.widget.OptionItemView;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.remote.GeneralCallback;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

public class UserInfoFragment extends Fragment {
    @BindView(R.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.mobileTextView)
    TextView mobileTextView;
    @BindView(R.id.nickyName)
    TextView nickyNameTextView;
    @BindView(R.id.chatButton)
    Button chatButton;
    //    @Bind(R.id.voipChatButton)
//    Button voipChatButton;
    @BindView(R.id.inviteButton)
    Button inviteButton;
    @BindView(R.id.aliasOptionItemView)
    SuperTextView aliasOptionItemView;

    @BindView(R.id.qrCodeOptionItemView)
    OptionItemView qrCodeOptionItemView;
    @BindView(R.id.gender)
    ImageView gender;
    private UserInfo userInfo;
    private UserViewModel userViewModel;
    private ContactViewModel contactViewModel;
    @BindView(R.id.delete)
    SuperTextView delete;
    @BindView(R.id.address)
    SuperTextView address;
    @BindView(R.id.set_black)
    SwitchButton setBlack;
    @BindView(R.id.send_card)
    SuperTextView sendCard;
    private PromptDialog promptDialog;


    public static UserInfoFragment newInstance(UserInfo userInfo) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("userInfo", userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        userInfo = args.getParcelable("userInfo");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        userViewModel =     ViewModelProviders.of(this).get(UserViewModel.class);
        contactViewModel =  ViewModelProviders.of(this).get(ContactViewModel.class);
        String selfUid = userViewModel.getUserId();
        promptDialog = new PromptDialog(getActivity());
        promptDialog.getAlertDefaultBuilder().textSize(12).textColor(ContextCompat.getColor(getActivity(), R.color.blue0));
        if (selfUid.equals(userInfo.uid)) {
            // self
            chatButton.setVisibility(View.GONE);
//            voipChatButton.setVisibility(View.GONE);
            inviteButton.setVisibility(View.GONE);
            qrCodeOptionItemView.setVisibility(View.VISIBLE);
            aliasOptionItemView.setVisibility(View.VISIBLE);
            setBlack.setVisibility(View.GONE);
        } else if (contactViewModel.isFriend(userInfo.uid)) {
            // friend
            setBlack.setVisibility(View.VISIBLE);
            chatButton.setVisibility(View.VISIBLE);
//            voipChatButton.setVisibility(View.VISIBLE);
            inviteButton.setVisibility(View.GONE);
        } else {
            // stranger
            setBlack.setVisibility(View.GONE);
            chatButton.setVisibility(View.GONE);
//            voipChatButton.setVisibility(View.GONE);
            inviteButton.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
            aliasOptionItemView.setVisibility(View.GONE);
        }

        setUserInfo(userInfo);
        userViewModel.userInfoLiveData().observe(this, userInfos -> {
            for (UserInfo info : userInfos) {
                if (userInfo.uid.equals(info.uid)) {
                    userInfo = info;
                    setUserInfo(info);
                    break;
                }
            }
        });
        userViewModel.getUserInfo(userInfo.uid, true);
        setBlack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    PromptButton cancle = new PromptButton("取消", button -> {
                        setBlack.setChecked(false);
                    });
                    cancle.setTextColor(Color.parseColor("#0076ff"));
                    promptDialog.showAlertSheet("是否添加到黑名单,", true, cancle,
                            new PromptButton("确定", button -> {
                                setBlack.setChecked(true);
                                ChatManager.Instance().setBlackList(userInfo.uid, true, new GeneralCallback() {
                                    @Override
                                    public void onSuccess() {
                                        ToastUtils.show("加入黑名单成功");
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onFail(int errorCode) {

                                    }
                                });

                            })

                    );

                }
            }
        });
    }

    private void setUserInfo(UserInfo userInfo) {

        GlideApp
                .with(this)
                .load(userInfo.portrait)
                .placeholder(R.mipmap.avatar_def)
                .transforms(new CenterCrop(), new RoundedCorners(10))
                .into(portraitImageView);
        nameTextView.setText(userInfo.name);
        nameTextView.setVisibility(View.GONE);
        nickyNameTextView.setText(userViewModel.getUserDisplayName(userInfo));
        address.setRightString(TextUtils.isEmpty(userInfo.address) ? "未知" : userInfo.address);
        mobileTextView.setText(TextUtils.isEmpty(userInfo.email)?"萌聊号:未设置":"萌聊号:" + userInfo.email);
        if (userInfo.gender==1){
            GlideApp.with(this).load(R.mipmap.ic_boy).into(gender);
        }else if (userInfo.gender==2){
            GlideApp.with(this).load(R.mipmap.ic_girl).into(gender);
        }else {
            gender.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.chatButton)
    void chat() {
        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        Conversation conversation = new Conversation(Conversation.ConversationType.Single, userInfo.uid, 0);
        intent.putExtra("conversation", conversation);
        startActivity(intent);
        getActivity().finish();
    }
    @OnClick(R.id.send_card)
    void sendCard() {
        Intent intent =new Intent(getActivity(), ContactListActivity.class);
        Conversation conversation = new Conversation(Conversation.ConversationType.Single, userInfo.uid, 0);
        intent.putExtra("conversation", conversation);
        startActivity(intent);

    }

    @OnClick(R.id.aliasOptionItemView)
    void alias() {
        String selfUid = userViewModel.getUserId();
        if (selfUid.equals(userInfo.uid)) {
            Intent intent = new Intent(getActivity(), ChangeMyNameActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SetAliasActivity.class);
            intent.putExtra("userId", userInfo.uid);
            startActivity(intent);
        }
    }

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    @OnClick(R.id.delete)
    void onDelete() {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.deleteFriend(userInfo.uid).observe(
                this, booleanOperateResult -> {
                    if (booleanOperateResult.isSuccess()) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtils.show("delete friend error " + booleanOperateResult.getErrorCode());
                    }
                }
        );
    }

    @OnClick(R.id.portraitImageView)
    void portrait() {
        if (userInfo.uid.equals(userViewModel.getUserId())) {
            updatePortrait();
        } else {
            // TODO show big portrait
        }
    }

    private void updatePortrait() {
        ImagePicker.picker().pick(this, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            String imagePath = ImageUtils.genThumbImgFile(images.get(0).path).getAbsolutePath();
            MutableLiveData<OperateResult<Boolean>> result = userViewModel.updateUserPortrait(imagePath);
            result.observe(this, booleanOperateResult -> {
                if (booleanOperateResult.isSuccess()) {
                    Toast.makeText(getActivity(), "更新头像成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "更新头像失败: " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.inviteButton)
    void invite() {
        Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.qrCodeOptionItemView)
    void showMyQRCode() {
        UserInfo userInfo = userViewModel.getUserInfo(userViewModel.getUserId(), false);
        String qrCodeValue = WfcScheme.QR_CODE_PREFIX_USER + userInfo.uid;
        startActivity(QRCodeActivity.buildQRCodeIntent(getActivity(), "二维码", userInfo.portrait, qrCodeValue));
    }
}
