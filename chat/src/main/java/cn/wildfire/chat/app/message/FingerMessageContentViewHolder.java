package cn.wildfire.chat.app.message;

import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfire.chat.kit.conversation.message.viewholder.NormalMessageContentViewHolder;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.FingerMessageContent;
@MessageContentType(FingerMessageContent.class)
@LayoutRes(resId = R.layout.conversation_item_finger)
@EnableContextMenu
public class FingerMessageContentViewHolder extends NormalMessageContentViewHolder {

    @BindView(R.id.finger)
    ImageView finger;

    public FingerMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        FingerMessageContent locationMessage = (FingerMessageContent) message.message.content;
        switch (locationMessage.getImageName()){
            case "finger1":
                Glide.with(fragment).load(R.mipmap.ic_finger_1).into(finger);
                break;
            case "finger2":
                Glide.with(fragment).load(R.mipmap.ic_finger_2).into(finger);
                break;
            case "finger3":
                Glide.with(fragment).load(R.mipmap.ic_finger_3).into(finger);
                break;
        }


    }
}
