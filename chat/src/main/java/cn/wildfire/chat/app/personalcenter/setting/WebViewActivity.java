package cn.wildfire.chat.app.personalcenter.setting;

import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.utils.PublicWebViewClient;
import cn.wildfirechat.chat.R;

/**
 * 网页加载页面
 */
public class WebViewActivity extends BaseActivity {
    private String url;
    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected int contentLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void init() {
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webView.getSettings().setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        webView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webView.getSettings().setAppCacheEnabled(false);//是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);//DOM Storage
        webView.getSettings().setBlockNetworkImage(false);
        webView.setWebViewClient(new PublicWebViewClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    String title = view.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        setTitle(title);
                    }
                }
            });
        } else {
            toolbarTitle.setText(title);
        }
    }
}
