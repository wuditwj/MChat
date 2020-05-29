package cn.wildfire.chat.kit.contact.newfriend;

import java.util.List;

import cn.wildfire.chat.kit.search.SearchActivity;
import cn.wildfire.chat.kit.search.SearchableModule;

/**
 * 添加好友
 */
public class SearchUserActivity extends SearchActivity {

    @Override
    protected void beforeViews() {
    }

    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        toolbarTitle.setText("添加好友");
        modules.add(new UserSearchModule());
    }
}
