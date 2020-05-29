package cn.wildfire.chat.app.group;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjq.toast.ToastUtils;
import com.mcxiaoke.bus.Bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.ConfigPath;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.group.GroupViewModel;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.notification.TransferGroupOwnerNotificationContent;
import cn.wildfirechat.model.GroupMember;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

@Route(path = ConfigPath.GroupMember)
public class GroupMembers extends WfcBaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.user_info)
    RecyclerView userInfo;
    @Autowired
    public int flag;
    @Autowired
    public String groupId;

    private GroupViewModel groupViewModel;

    @Override
    protected int contentLayout() {
        return R.layout.activity_group_members;
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        userInfo.setLayoutManager(new LinearLayoutManager(this));
        userInfo.addItemDecoration(new ItemDecorationDivider(this,ItemDecorationDivider.VERTICAL_LIST
                ,1, ContextCompat.getColor(this,R.color.line)));
        switch (flag){
            case 0:
                title.setText("谁可以领");
                break;
            case 1:
                title.setText("群主转让");
                break;
        }
       List<GroupMember> groupMembers =  ChatManager.Instance().getGroupMembers(groupId,true);
        List<String>uids = new ArrayList<>();
        for (int i = 0; i < groupMembers.size(); i++) {
            if (!ChatManager.Instance().getUserId().equals(groupMembers.get(i).memberId)){
                uids.add(groupMembers.get(i).memberId);
            }

        }
        List<UserInfo> userInfos =  ChatManager.Instance().getUserInfos(uids,groupId);
        GroupMembersAdapter friendAdapter = new GroupMembersAdapter(R.layout.item_friend,userInfos);
        userInfo.setAdapter(friendAdapter);
        friendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                switch (flag){
                    case 0:
                        Intent intent = new Intent();
                        Bundle bundle= new Bundle();
                        bundle.putParcelable("userInfo",userInfos.get(position));
                        intent.putExtras(bundle);
                        setResult(1000,intent);
                        finish();
                        break;
                    case 1:
                        TransferGroupOwnerNotificationContent transferGroupOwnerNotificationContent = new TransferGroupOwnerNotificationContent();
                        transferGroupOwnerNotificationContent.newOwner=userInfos.get(position).uid;
                        transferGroupOwnerNotificationContent.operator=ChatManager.Instance().getUserId();
                        transferGroupOwnerNotificationContent.groupId=groupId;
                        onTransferGroup(transferGroupOwnerNotificationContent,userInfos.get(position).uid);
                        break;
                }

            }
        });

    }
    private void onTransferGroup(TransferGroupOwnerNotificationContent transferGroupOwnerNotificationContent,String uid){
        groupViewModel.setTransferGroup(groupId,uid, Collections.singletonList(0),
                transferGroupOwnerNotificationContent).observe(this, booleanOperateResult -> {
            if (booleanOperateResult.isSuccess()) {
                Bus.getDefault().post(transferGroupOwnerNotificationContent);
                ToastUtils.show("转让成功");
                finish();
            } else {
                ToastUtils.show("转让群主错误码 " + booleanOperateResult.getErrorCode());
            }
        });
    }
}
