package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.wildfire.chat.app.entity.RedEnvelopeEntity;
import cn.wildfire.chat.app.envelope.RedPackageActivity;
import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.Conversation;

import static android.app.Activity.RESULT_OK;

public class RedPackageExt extends ConversationExt {

    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "[红包]")
    public void pickLocation(View containerView, Conversation conversation) {

        Intent intent = new Intent(activity, RedPackageActivity.class);
        intent.putExtra("conversation",conversation);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            RedEnvelopeEntity locationData =  data.getParcelableExtra("redEnvelopeVo");
            messageViewModel.sendRedPackage(conversation,locationData);
        }
    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int iconResId() {
        return R.mipmap.ic_red_packge;
    }

    @Override
    public String title(Context context) {
        return "红包";
    }
}


