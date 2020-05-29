package cn.wildfire.chat.kit.contact.newfriend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.wildfire.chat.kit.contact.model.UIUserInfo;
import cn.wildfire.chat.kit.contact.viewholder.UserViewHolder;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfire.chat.kit.search.SearchableModule;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfire.chat.kit.viewmodel.MessageViewModel;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.remote.SendMessageCallback;

public class SearchFriendModule extends SearchableModule<UserInfo, UserViewHolder> {
    private boolean sendCard;
    private Context context;
    private MessageViewModel messageViewModel;
    private Conversation conversation;

    public SearchFriendModule(boolean sendCard, Context context, MessageViewModel messageViewModel, Conversation conversation) {
        this.sendCard = sendCard;
        this.context = context;
        this.messageViewModel = messageViewModel;
        this.conversation = conversation;
    }



    @Override
    public UserViewHolder onCreateViewHolder(Fragment fragment, @NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.search_item_contact, parent, false);
        return new UserViewHolder(fragment, null, itemView);
    }

    @Override
    public void onBind(Fragment fragment, UserViewHolder holder, UserInfo userInfo) {
        holder.onBind(new UIUserInfo(userInfo));
    }

    @Override
    public void onClick(Fragment fragment, UserViewHolder holder, View view, UserInfo userInfo) {
        if (sendCard){
            //发送名片
            //发送名片
            //把名片发给userInfo
            Conversation conversation1 = new Conversation(Conversation.ConversationType.Single, userInfo.uid);
            messageViewModel.sendCard(conversation,conversation.target, new SendMessageCallback() {
                @Override
                public void onSuccess(long messageUid, long timestamp) {
                    Intent intent = new Intent(context, ConversationActivity.class);
                    intent.putExtra("conversation", conversation1);
                    context.startActivity(intent);
                }

                @Override
                public void onFail(int errorCode) {

                }

                @Override
                public void onPrepare(long messageId, long savedTime) {

                }
            });


        }else {
            Intent intent = new Intent(fragment.getActivity(), UserInfoActivity.class);
            intent.putExtra("userInfo", userInfo);
            fragment.startActivity(intent);
        }

    }

    @Override
    public int getViewType(UserInfo userInfo) {
        return R.layout.contact_item_contact;
    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public boolean expandable() {
        return false;
    }

    @Override
    public String category() {
        return null;
    }

    @Override
    public List<UserInfo> search(String keyword) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<UserInfo> userInfos = ChatManager.Instance().searchFriends(keyword);
        countDownLatch.countDown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userInfos;
    }
}

