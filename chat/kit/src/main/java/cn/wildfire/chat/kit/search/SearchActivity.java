package cn.wildfire.chat.kit.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcBaseNoToolbarActivity;
import cn.wildfire.chat.kit.widget.SearchView;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 如果启动{@link Intent}里面包含keyword，直接开始搜索
 */
public abstract class SearchActivity extends WfcBaseNoToolbarActivity {
    @BindView(R.id.root_search_view)
    LinearLayout rootSearchView;
    @BindView(R.id.toolBar_top)
    View toolBarTop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    protected TextView toolbarTitle;
    private SearchFragment searchFragment;
    private List<SearchableModule> modules = new ArrayList<>();

    @BindView(R.id.search_view)
    SearchView searchView;

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        finish();
    }

    /**
     * 子类如果替换布局，它的布局中必须要包含 R.layout.search_bar
     *
     * @return 布局资源id
     */
    protected int contentLayout() {
        return R.layout.search_portal_activity;
    }

    protected void beforeViews() {
        setStatusBarColor(R.color.gray5);
    }

    protected void afterViews() {
        initSearchView();
        initSearchFragment();

//        ImmersionBar.setTitleBar(this, rootSearchView);
        ImmersionBar.with(this).statusBarView(toolBarTop).init();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String initialKeyword = getIntent().getStringExtra("keyword");
        ChatManager.Instance().getMainHandler().post(() -> {
            if (!TextUtils.isEmpty(initialKeyword)) {
                searchView.setQuery(initialKeyword);
            }
        });
    }


    private void initSearchView() {
        searchView.setOnQueryTextListener(this::search);
    }

    private void initSearchFragment() {
        searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, searchFragment)
                .commit();
        initSearchModule(modules);
    }

    void search(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            searchFragment.search(keyword, modules);
        } else {
            searchFragment.reset();
        }
    }

    /**
     * @param modules 是一个输出参数，用来添加希望搜索的{@link SearchableModule}
     */
    protected abstract void initSearchModule(List<SearchableModule> modules);

}
