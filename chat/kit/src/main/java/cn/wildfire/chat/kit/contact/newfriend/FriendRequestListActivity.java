package cn.wildfire.chat.kit.contact.newfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.chat.R;

public class FriendRequestListActivity extends WfcBaseActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void afterViews() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, new FriendRequestListFragment())
                .commit();
        title.setText("新朋友");
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    @Override
    protected int menu() {
        return R.menu.contact_friend_request;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            addContact();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void addContact() {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }
}
