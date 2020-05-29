package cn.wildfire.chat.kit.contact.newfriend;

import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import cn.wildfire.chat.kit.search.SearchActivity;
import cn.wildfire.chat.kit.search.SearchableModule;
import cn.wildfire.chat.kit.viewmodel.MessageViewModel;
import cn.wildfirechat.model.Conversation;

public class SearchFriendActivity extends SearchActivity {
    private boolean sendCard;
    private MessageViewModel messageViewModel;
    private Conversation conversation;
    @Override
    protected void beforeViews() {
        sendCard = getIntent().getBooleanExtra("sendCard",false);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        conversation = getIntent().getParcelableExtra("conversation");
    }
    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        toolbarTitle.setText("搜索");
        modules.add(new SearchFriendModule(sendCard,this,messageViewModel,conversation));
    }
}
