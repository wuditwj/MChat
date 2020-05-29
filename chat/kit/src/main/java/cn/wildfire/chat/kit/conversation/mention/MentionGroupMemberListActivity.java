package cn.wildfire.chat.kit.conversation.mention;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.group.GroupViewModel;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.UserInfo;

import static cn.wildfire.chat.kit.conversation.ConversationFragment.REQUEST_PICK_MENTION_CONTACT;

/**
 * @m某人跳转的页面(列表)
 */
public class MentionGroupMemberListActivity extends WfcBaseActivity {
    @BindView(R.id.search)
    LinearLayout search;
    private GroupInfo groupInfo;

    @Override
    protected void afterViews() {
        super.afterViews();
        groupInfo = getIntent().getParcelableExtra("groupInfo");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, MentionGroupMemberFragment.newInstance(groupInfo))
                .commit();
    }

    @Override
    protected int contentLayout() {
        return R.layout.mention_group_member_activity;
    }

    @OnClick(R.id.search)
    public void onViewClicked() {
        Intent intent = new Intent(this, MentionGroupMemberActivity.class);
        intent.putExtra("groupInfo", groupInfo);
        startActivityForResult(intent, REQUEST_PICK_MENTION_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PICK_MENTION_CONTACT) {
                String userId = data.getStringExtra("userId");
                Intent intent2 = new Intent();
                intent2.putExtra("userId", userId);
                setResult(Activity.RESULT_OK, intent2);
                finish();
            }
        }
    }
}
