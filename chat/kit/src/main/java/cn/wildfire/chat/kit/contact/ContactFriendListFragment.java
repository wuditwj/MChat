package cn.wildfire.chat.kit.contact;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.hjq.toast.ToastUtils;

import cn.wildfire.chat.app.MainActivity;
import cn.wildfire.chat.kit.channel.ChannelListActivity;
import cn.wildfire.chat.kit.contact.model.ContactCountFooterValue;
import cn.wildfire.chat.kit.contact.model.FriendRequestValue;
import cn.wildfire.chat.kit.contact.model.GroupValue;
import cn.wildfire.chat.kit.contact.model.UIUserInfo;
import cn.wildfire.chat.kit.contact.newfriend.FriendRequestListActivity;
import cn.wildfire.chat.kit.contact.newfriend.SearchFriendActivity;
import cn.wildfire.chat.kit.contact.newfriend.SearchUserActivity;
import cn.wildfire.chat.kit.contact.viewholder.footer.ContactCountViewHolder;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfire.chat.kit.group.GroupListActivity;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfire.chat.kit.viewmodel.MessageViewModel;
import cn.wildfire.chat.kit.widget.QuickIndexBar;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.remote.SendMessageCallback;

public class ContactFriendListFragment extends BaseUserListFragment implements QuickIndexBar.OnLetterUpdateListener {
    private MessageViewModel messageViewModel;
    private Conversation conversation;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (userListAdapter != null && isVisibleToUser) {
            contactViewModel.reloadContact();
            contactViewModel.reloadFriendRequestStatus();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        contactViewModel.reloadContact();
        contactViewModel.reloadFriendRequestStatus();
    }

    @Override
    protected void afterViews(View view) {
        super.afterViews(view);
        conversation = getActivity().getIntent().getParcelableExtra("conversation");
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        contactViewModel.contactListLiveData().observe(this, userInfos -> {
            showContent();
            userListAdapter.setUsers(userInfos);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void initHeaderViewHolders() {
        addHeaderViewHolder(ContractSearchTitleHolder.class, new FriendRequestValue());//通讯录头部
    }

    @Override
    public void initFooterViewHolders() {
        addFooterViewHolder(ContactCountViewHolder.class, new ContactCountFooterValue());

    }

    @Override
    public void onUserClick(UIUserInfo userInfo) {
        //发送名片
        //把名片发给userInfo
        if(conversation!=null){
            Conversation conversation1 = new Conversation(Conversation.ConversationType.Single, userInfo.getUserInfo().uid);
            messageViewModel.sendCard(conversation1,conversation.target, new SendMessageCallback() {
                @Override
                public void onSuccess(long messageUid, long timestamp) {
                    Intent intent = new Intent(getActivity(), ConversationActivity.class);
                    intent.putExtra("conversation", conversation1);
                    startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onFail(int errorCode) {

                }

                @Override
                public void onPrepare(long messageId, long savedTime) {

                }
            });
        }else {
            //查看好友信息
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            intent.putExtra("userInfo", userInfo.getUserInfo());
            startActivity(intent);
        }


    }

    @Override
    public void onHeaderClick(int index) {
        switch (index) {
            case 0://好友搜索
                Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
                intent.putExtra("sendCard",conversation!=null);
                intent.putExtra("conversation", conversation);
                startActivity(intent);
                break;
        }
    }

    private void showFriendRequest() {
        FriendRequestValue value = new FriendRequestValue(0);
        userListAdapter.updateHeader(0, value);

        contactViewModel.clearUnreadFriendRequestStatus();
        Intent intent = new Intent(getActivity(), FriendRequestListActivity.class);
        startActivity(intent);
    }

    private void showGroupList() {
        Intent intent = new Intent(getActivity(), GroupListActivity.class);
        startActivity(intent);
    }

    private void showChannelList() {
        Intent intent = new Intent(getActivity(), ChannelListActivity.class);
        startActivity(intent);
    }
}
