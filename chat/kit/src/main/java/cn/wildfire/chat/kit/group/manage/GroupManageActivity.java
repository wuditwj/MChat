package cn.wildfire.chat.kit.group.manage;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.GroupInfo;

public class GroupManageActivity extends WfcBaseActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void afterViews() {
        GroupInfo groupInfo = getIntent().getParcelableExtra("groupInfo");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, GroupManageFragment.newInstance(groupInfo))
                .commit();
        title.setText("群管理");
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }
}
