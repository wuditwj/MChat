package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.view.View;

import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.Conversation;

public class ShakeExt extends ConversationExt {

    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "[戳一戳]")
    public void pickLocation(View containerView, Conversation conversation) {
        //发送抖动戳一戳

        messageViewModel.sendshakeMessage(conversation,conversation.target, ChatManagerHolder.gChatManager.getUserId());

    }


    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int iconResId() {
        return R.mipmap.ic_fun_shake;
    }

    @Override
    public String title(Context context) {
        return "戳一戳";
    }
}

