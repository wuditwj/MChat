package cn.wildfire.chat.kit;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.contact.ContactFriendListFragment;
import cn.wildfirechat.chat.R;

public class ContactListActivity extends WfcBaseActivity {
    @BindView(R.id.left_menu)
    TextView leftMenu;

    @Override
    protected void afterViews() {
        leftMenu.setText("取消");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, new ContactFriendListFragment())
                .commit();
        toolbar.setNavigationIcon(null);
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }


    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }
}
