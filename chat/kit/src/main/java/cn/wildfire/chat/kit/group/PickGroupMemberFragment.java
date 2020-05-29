package cn.wildfire.chat.kit.group;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import cn.wildfire.chat.kit.contact.model.UIUserInfo;
import cn.wildfire.chat.kit.contact.pick.PickUserFragment;
import cn.wildfire.chat.kit.contact.pick.PickUserViewModel;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.GroupMember;

public class PickGroupMemberFragment extends PickUserFragment {
    private GroupInfo groupInfo;
    private static boolean foot;

    public static PickGroupMemberFragment newInstance(GroupInfo groupInfo, boolean f) {
        Bundle args = new Bundle();
        args.putParcelable("groupInfo", groupInfo);
        PickGroupMemberFragment fragment = new PickGroupMemberFragment();
        fragment.setArguments(args);
        foot = f;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupInfo = getArguments().getParcelable("groupInfo");
    }

    @Override
    protected void setupPickFromUsers() {
        PickUserViewModel pickUserViewModel = ViewModelProviders.of(getActivity()).get(PickUserViewModel.class);
        GroupViewModel groupViewModel = ViewModelProviders.of(getActivity()).get(GroupViewModel.class);
        groupViewModel.getGroupMemberUIUserInfosLiveData(groupInfo.target, false).observe(this, uiUserInfos -> {
            showContent();

            /**
             * 判断是不是管理员,管理员不显示
             */
            List<UIUserInfo> list = new ArrayList<>();
            if (foot) {
                for (UIUserInfo user : uiUserInfos) {
                    GroupMember me = groupViewModel.getGroupMember(groupInfo.target, user.getUserInfo().uid);
                    if (me.type != GroupMember.GroupMemberType.Manager) {
                        list.add(user);
                    }
                }
                pickUserViewModel.setUsers(list);
                userListAdapter.setUsers(list);
            } else {
                pickUserViewModel.setUsers(uiUserInfos);
                userListAdapter.setUsers(uiUserInfos);
            }
        });
    }
}
