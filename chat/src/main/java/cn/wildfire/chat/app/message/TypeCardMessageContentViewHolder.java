package cn.wildfire.chat.app.message;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.annotation.ReceiveLayoutRes;
import cn.wildfire.chat.kit.annotation.SendLayoutRes;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfire.chat.kit.conversation.message.viewholder.NormalMessageContentViewHolder;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.TypeCardMessageContent;
import cn.wildfirechat.model.UserInfo;

@MessageContentType(TypeCardMessageContent.class)
@SendLayoutRes(resId = R.layout.conversation_item_type_card_send)
@ReceiveLayoutRes(resId = R.layout.conversation_item_type_card_receive)
@EnableContextMenu
public class TypeCardMessageContentViewHolder extends NormalMessageContentViewHolder {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.bg_package)
    LinearLayout bgPackage;
    @BindView(R.id.key)
    ImageView key;

    public TypeCardMessageContentViewHolder(ConversationFragment context, RecyclerView.Adapter adapter, View itemView) {
        super(context, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        TypeCardMessageContent typeCardMessageContent = (TypeCardMessageContent) message.message.content;
        String user_id = typeCardMessageContent.getCardUserId();
        UserInfo userInfo = ChatManagerHolder.gChatManager.getUserInfo(user_id,true);
        name.setText(userInfo.displayName);
        GlideApp.with(fragment).load(userInfo.portrait).into(key);

    }

    @OnClick(R.id.locationLinearLayout)
    public void onClick(View view) {
        TypeCardMessageContent typeCardMessageContent = (TypeCardMessageContent) message.message.content;
        UserInfo userInfo = ChatManagerHolder.gChatManager.getUserInfo(typeCardMessageContent.getCardUserId(),true);
        Intent intent = new Intent(fragment.getContext(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        fragment.getContext().startActivity(intent);


    }
}
