package cn.wildfire.chat.app.message.redpackage;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.daasuu.bl.BubbleLayout;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.ConfigPath;
import cn.wildfire.chat.app.dialog.RedPackageDialog;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.annotation.ReceiveLayoutRes;
import cn.wildfire.chat.kit.annotation.SendLayoutRes;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfire.chat.kit.conversation.message.viewholder.NormalMessageContentViewHolder;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.RedPackageMessageContent;
import cn.wildfirechat.remote.ChatManager;

@MessageContentType(RedPackageMessageContent.class)
@SendLayoutRes(resId = R.layout.conversation_item_red_package_send)
@ReceiveLayoutRes(resId = R.layout.conversation_item_red_package_receive)
@EnableContextMenu
public class RedPackageMessageContentViewHolder extends NormalMessageContentViewHolder {

    @BindView(R.id.locationTitleTextView)
    TextView locationTitleTextView;
    @BindView(R.id.locationLinearLayout)
    BubbleLayout bgPackage;
    @BindView(R.id.key)
    ImageView key;
    private RedPackageDialog redPackageDialog;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.get_red)
    TextView getRed;

    public RedPackageMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }


    @Override
    public void onBind(UiMessage message) {
        RedPackageMessageContent locationMessage = (RedPackageMessageContent) message.message.content;
        if (TextUtils.equals("3", locationMessage.getSendType())) {
            status.setText("萌聊红包(专属红包)" + locationMessage.getNickName() + "领取");
        }
        locationTitleTextView.setText(locationMessage.getRedTitle());
//        //获取领取红包状态
        if (!TextUtils.equals(locationMessage.getReceiveStatus(), "2")) {
            bgPackage.setBubbleColor(Color.parseColor("#FF5C5C"));
            key.setBackgroundResource(R.mipmap.ic_money);
        } else {
            if (locationMessage.getFormUser().equals(ChatManager.Instance().getUserId())) {
                getRed.setText("红包已领完");
            } else {
                getRed.setText("已领取");
            }
            bgPackage.setBubbleColor(Color.parseColor("#FFA4A4"));
            key.setBackgroundResource(R.mipmap.ic_red_open);
        }
    }

    @OnClick(R.id.locationLinearLayout)
    public void onClick(View view) {
        //领取红包 调用接口  改变红包状态  通知消息更改
        RedPackageMessageContent locationMessage = (RedPackageMessageContent) message.message.content;
        //获取红包状态
        checkRedStatus(locationMessage);
    }

    private void checkRedStatus(RedPackageMessageContent redPackageMessageContent) {
        ApiMethodFactory.getInstance().checkRedPackageStatus(redPackageMessageContent.getSendLogId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> baseBean = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (baseBean.getCode() == 600) {
                    ToastUtils.show("红包超24小时无法查看");
                } else {
                    redPackageDialog = new RedPackageDialog(fragment.getContext(), baseBean.getCode(),
                            redPackageMessageContent.getRedTitle(), redPackageMessageContent.getFormUser(),
                            () -> {
                                ApiMethodFactory.getInstance().getRedPackage(ChatManagerHolder.gChatManager.getUserId(),
                                        redPackageMessageContent.getSendLogId(), redPackageMessageContent
                                                .getSendType(), response1 -> {
                                            BaseBean<String> json = JSONObject.parseObject(response1, new TypeReference<BaseBean<String>>() {
                                            });
                                            if (baseBean.getCode() == 200) {
                                                ARouter.getInstance()
                                                        .build(ConfigPath.RedPackageDetail)
                                                        .withString("sendLogId", redPackageMessageContent.getSendLogId())
                                                        .withString("robUser", ChatManagerHolder.gChatManager.getUserId())
                                                        .navigation(fragment.getContext());
                                                messageViewModel.modifyRedPackageStatus(message);
                                                // 被领取的红包的Uid

                                                //领取人Id //接受人

                                                //fromId 发送人ID
                                                messageViewModel.sendRobRedMessage(message.message.conversation, redPackageMessageContent.getSendLogId(),
                                                        ChatManagerHolder.gChatManager.getUserId(),
                                                        message.message.sender, message.message.serverTime / 1000, System.currentTimeMillis() / 1000);
                                            } else {
                                                ARouter.getInstance()
                                                        .build(ConfigPath.RedPackageDetail)
                                                        .withString("sendLogId", redPackageMessageContent.getSendLogId())
                                                        .withString("robUser", ChatManagerHolder.gChatManager.getUserId())
                                                        .withInt("code", baseBean.getCode())
                                                        .navigation(fragment.getContext());
                                                messageViewModel.modifyRedPackageStatus(message);
                                            }
                                            redPackageDialog.dismiss();

                                        });
                            });
                    String sender = message.message.sender;
                    switch (redPackageMessageContent.getSendType()) {
                        case "2"://群发 自己可以领取
                            if (!redPackageDialog.isShowing()) {
                                if (!TextUtils.equals("2", redPackageMessageContent.getReceiveStatus())) {
                                    redPackageDialog.show();
                                } else {
                                    //已经领取红包去查看详情
                                    ARouter.getInstance()
                                            .build(ConfigPath.RedPackageDetail)
                                            .withString("sendLogId", redPackageMessageContent.getSendLogId())
                                            .withString("robUser", ChatManagerHolder.gChatManager.getUserId())
                                            .withInt("code", baseBean.getCode())
                                            .navigation(fragment.getContext());
                                }
                            }
                            break;
                        case "3":
                            //定向红包
                            if (TextUtils.equals(ChatManagerHolder.gChatManager.getUserId(), redPackageMessageContent.getToUserId())) {
                                if (!redPackageDialog.isShowing()) {
                                    if (!TextUtils.equals("2", redPackageMessageContent.getReceiveStatus())) {
                                        redPackageDialog.show();
                                    } else {
                                        //已经领取红包去查看详情
                                        ARouter.getInstance()
                                                .build(ConfigPath.RedPackageDetail)
                                                .withString("sendLogId", redPackageMessageContent.getSendLogId())
                                                .withString("robUser", ChatManagerHolder.gChatManager.getUserId())
                                                .withInt("code", baseBean.getCode())
                                                .navigation(fragment.getContext());
                                    }
                                }
                            } else {
                                if (TextUtils.equals(ChatManagerHolder.gChatManager.getUserId(), redPackageMessageContent.getFormUser())) {
                                    ARouter.getInstance()
                                            .build(ConfigPath.RedPackageDetail)
                                            .withString("sendLogId", redPackageMessageContent.getSendLogId())
                                            .withString("robUser", ChatManagerHolder.gChatManager.getUserId())
                                            .withInt("code", baseBean.getCode())
                                            .navigation(fragment.getContext());
                                } else {
                                    ToastUtils.show("当前为专属红包,您不能领取");
                                }


                            }
                            break;
                        default://  单独发自己不能领取
                            if (!ChatManagerHolder.gChatManager.getUserId().equals(sender)) {
                                if (!redPackageDialog.isShowing()) {
                                    if (!TextUtils.equals("2", redPackageMessageContent.getReceiveStatus())) {
                                        redPackageDialog.show();
                                    } else {
                                        //已经领取红包去查看详情
                                        ARouter.getInstance()
                                                .build(ConfigPath.RedPackageDetail)
                                                .withString("sendLogId", redPackageMessageContent.getSendLogId())
                                                .withString("robUser", ChatManagerHolder.gChatManager.getUserId())
                                                .withInt("code", baseBean.getCode())
                                                .navigation(fragment.getContext());
                                    }
                                }

                            } else {
                                //去查看自己发的红包
                                ARouter.getInstance()
                                        .build(ConfigPath.RedPackageDetail)
                                        .withString("sendLogId", redPackageMessageContent.getSendLogId())
                                        .withString("robUser", ChatManagerHolder.gChatManager.getUserId())
                                        .withInt("red_Type", 1)
                                        .navigation(fragment.getContext());
                            }

                            break;
                    }
                }
            }
        });
    }
}
