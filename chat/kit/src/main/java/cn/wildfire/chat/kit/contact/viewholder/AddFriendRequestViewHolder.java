package cn.wildfire.chat.kit.contact.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.contact.UserListAdapter;
import cn.wildfire.chat.kit.contact.model.FriendRequestValue;
import cn.wildfire.chat.kit.contact.viewholder.header.HeaderViewHolder;
import cn.wildfirechat.chat.R;

@SuppressWarnings("unused")
@LayoutRes(resId = R.layout.contact_header_add_friend)
public class AddFriendRequestViewHolder extends HeaderViewHolder<FriendRequestValue> {
    @BindView(R.id.unreadFriendRequestCountTextView)
    TextView unreadRequestCountTextView;
    private FriendRequestValue value;


    public AddFriendRequestViewHolder(Fragment fragment, UserListAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(FriendRequestValue value) {
        this.value = value;
        if (value.getUnreadRequestCount() > 0) {
            unreadRequestCountTextView.setVisibility(View.VISIBLE);
            unreadRequestCountTextView.setText("" + value.getUnreadRequestCount());
        } else {
            unreadRequestCountTextView.setVisibility(View.GONE);
        }
    }
}
