package cn.wildfire.chat.kit.contact.viewholder.header;

import android.view.View;

import androidx.fragment.app.Fragment;

import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.contact.UserListAdapter;
import cn.wildfire.chat.kit.contact.model.FriendRequestValue;
import cn.wildfirechat.chat.R;

/**
 * 通讯录头部标题栏/搜索
 */
@SuppressWarnings("unused")
@LayoutRes(resId = R.layout.contact_title_group)
public class ContractTitleHolder extends HeaderViewHolder<FriendRequestValue> {

    public ContractTitleHolder(Fragment fragment, UserListAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(FriendRequestValue groupValue) {
    }
}
