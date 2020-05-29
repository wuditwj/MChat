package cn.wildfire.chat.kit.search;

import java.util.List;

import cn.wildfire.chat.kit.search.module.ConversationMessageSearchModule;
import cn.wildfirechat.model.Conversation;

/**
 * 搜索聊天记录
 */
public class SearchMessageActivity extends SearchActivity {
    private Conversation conversation;

    @Override
    protected void beforeViews() {
        conversation = getIntent().getParcelableExtra("conversation");
    }

    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        toolbarTitle.setText("查找聊天记录");
        modules.add(new ConversationMessageSearchModule(conversation));
    }
}
