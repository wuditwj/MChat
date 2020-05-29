package cn.wildfire.chat.kit.conversation.message.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.AddParticipantsMessageContent;
import cn.wildfirechat.message.SpannableStringUtils;
import cn.wildfirechat.message.Utils;
import cn.wildfirechat.message.core.MessageStatus;
import cn.wildfirechat.message.notification.AddGroupMemberNotificationContent;
import cn.wildfirechat.message.notification.ChangeGroupNameNotificationContent;
import cn.wildfirechat.message.notification.ChangeGroupPortraitNotificationContent;
import cn.wildfirechat.message.notification.CreateGroupNotificationContent;
import cn.wildfirechat.message.notification.DismissGroupNotificationContent;
import cn.wildfirechat.message.notification.GroupJoinTypeNotificationContent;
import cn.wildfirechat.message.notification.GroupMuteNotificationContent;
import cn.wildfirechat.message.notification.GroupPrivateChatNotificationContent;
import cn.wildfirechat.message.notification.GroupSetManagerNotificationContent;
import cn.wildfirechat.message.notification.KickoffGroupMemberNotificationContent;
import cn.wildfirechat.message.notification.ModifyGroupAliasNotificationContent;
import cn.wildfirechat.message.notification.NotificationMessageContent;
import cn.wildfirechat.message.notification.QuitGroupNotificationContent;
import cn.wildfirechat.message.notification.RecallMessageContent;
import cn.wildfirechat.message.notification.RobRedMessageContent;
import cn.wildfirechat.message.notification.ShakeMessageContent;
import cn.wildfirechat.message.notification.TipNotificationContent;
import cn.wildfirechat.message.notification.TransferGroupOwnerNotificationContent;
import cn.wildfirechat.remote.ChatManager;

@MessageContentType(value = {
        AddGroupMemberNotificationContent.class,
        ChangeGroupNameNotificationContent.class,
        ChangeGroupPortraitNotificationContent.class,
        CreateGroupNotificationContent.class,
        DismissGroupNotificationContent.class,
        DismissGroupNotificationContent.class,
        KickoffGroupMemberNotificationContent.class,
        ModifyGroupAliasNotificationContent.class,
        QuitGroupNotificationContent.class,
        TransferGroupOwnerNotificationContent.class,
        TipNotificationContent.class,
        RecallMessageContent.class,
        GroupMuteNotificationContent.class,
        GroupPrivateChatNotificationContent.class,
        GroupJoinTypeNotificationContent.class,
        GroupSetManagerNotificationContent.class,
        AddParticipantsMessageContent.class,
        // TODO add more
        RobRedMessageContent.class,
        ShakeMessageContent.class

})
@LayoutRes(resId = R.layout.conversation_item_notification)
/**
 * 小灰条消息, 居中显示，且不显示发送者，用于简单通知，如果需要扩展成复杂通知，可以参考 {@link ExampleRichNotificationMessageContentViewHolder}
 *
 */
public class SimpleNotificationMessageContentViewHolder extends NotificationMessageContentViewHolder {

    @BindView(R.id.notificationTextView)
    TextView notificationTextView;
    @BindView(R.id.red_icon)
    ImageView redIcon;

    public SimpleNotificationMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message, int position) {
        super.onBind(message, position);
        onBind(message);
    }

    @Override
    public boolean contextMenuItemFilter(UiMessage uiMessage, String tag) {
        return true;
    }

    protected void onBind(UiMessage message) {
        String notification;
        try {
            notification = ((NotificationMessageContent) message.message.content).formatNotification(message.message);
        } catch (Exception e) {
            e.printStackTrace();
            notification = "message is invalid";
        }
        if (notification.contains("戳")) {
            if (message.message.status != MessageStatus.Readed && message.message.status != MessageStatus.Sent) {
                Vibrator vibrator = (Vibrator) fragment.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
                vibrator.vibrate(pattern, -1);
            }
        }

        if (message.message.content instanceof RobRedMessageContent) {
            notificationTextView.setBackground(null);
            notificationTextView.setTextColor(Color.parseColor("#7e7e7e"));
            //关于红包的消息/小红包图标
            redIcon.setVisibility(View.VISIBLE);
            String messageContent = message.message.digest();
            if (!messageContent.equals("")) {
                notificationTextView.setText(SpannableStringUtils.getBuilder(messageContent.substring(0, messageContent.length() - 2))
                        .append(messageContent.substring(messageContent.length() - 2)).setForegroundColor(Color.parseColor("#DF5050"))
                        .create());
            } else {
                redIcon.setVisibility(View.GONE);
            }

            if (TextUtils.equals(ChatManager.Instance().getUserId(), ((RobRedMessageContent) message.message.content).getFromId())) {
                notificationTextView.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(((RobRedMessageContent) message.message.content).getReceiverId(), ChatManager.Instance().getUserId())) {
                notificationTextView.setVisibility(View.VISIBLE);
            } else {
                notificationTextView.setVisibility(View.GONE);
            }
        } else {
            notificationTextView.setText(notification);
        }

    }
}
