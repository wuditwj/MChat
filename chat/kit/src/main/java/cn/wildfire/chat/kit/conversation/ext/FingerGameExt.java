package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.Conversation;

public class FingerGameExt extends ConversationExt {

    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "[猜拳]")
    public void pickLocation(View containerView, Conversation conversation) {
        List<String> list = new ArrayList<>();
        list.add("finger1");
        list.add("finger2");
        list.add("finger3");
        Random rand = new Random();
        String fingerName = list.get(rand.nextInt(list.size()));

        messageViewModel.sendFingerMessage(conversation,fingerName);

    }


    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int iconResId() {
        return R.mipmap.ic_fun_finger;
    }

    @Override
    public String title(Context context) {
        return "猜拳";
    }
}


