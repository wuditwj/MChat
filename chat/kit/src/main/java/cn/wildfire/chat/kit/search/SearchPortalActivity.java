package cn.wildfire.chat.kit.search;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.List;

import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.ConfigPath;
import cn.wildfire.chat.kit.search.module.ChannelSearchModule;
import cn.wildfire.chat.kit.search.module.ContactSearchModule;
import cn.wildfire.chat.kit.search.module.ConversationSearchModule;
import cn.wildfire.chat.kit.search.module.GroupSearchViewModule;

/**
 * 主页搜索
 */
@Route(path = ConfigPath.SearchPortal)
public class SearchPortalActivity extends SearchActivity {
    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        toolbarTitle.setText("搜索");
        SearchableModule module = new ContactSearchModule();
        modules.add(module);

        module = new GroupSearchViewModule();
        modules.add(module);

        module = new ConversationSearchModule();
        modules.add(module);
        modules.add(new ChannelSearchModule());
    }
}
