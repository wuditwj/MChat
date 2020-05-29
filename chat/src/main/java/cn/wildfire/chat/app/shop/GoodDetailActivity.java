package cn.wildfire.chat.app.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zrq.spanbuilder.Spans;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.entity.GoodsCartVo;
import cn.wildfire.chat.app.shop.entity.GoodsMallVo;
import cn.wildfire.chat.app.shop.utils.GlideImageLoader;
import cn.wildfire.chat.app.shop.utils.PublicWebViewClient;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfirechat.chat.R;

/**
 * 商品详情页面
 */
public class GoodDetailActivity extends BaseActivity {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mall_price)
    TextView mallPrice;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.buy)
    TextView buy;

    private WebSettings mWebSettings;
    private String goods_id;
    private List<String> list = new ArrayList<>();

    @Override
    protected int contentLayout() {
        return R.layout.activity_good_detail;
    }

    protected void init() {
        toolbarTitle.setText("商品详情");
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
        layoutParams.height = width;
        banner.setLayoutParams(layoutParams);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImageLoader(new GlideImageLoader());
        goods_id = getIntent().getStringExtra("goods_id");
        mWebSettings = webView.getSettings();
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setBuiltInZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient());
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setSupportZoom(true);//是否可以缩放，默认true
        mWebSettings.setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        mWebSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(true);//是否使用缓存
        mWebSettings.setUserAgentString(System.getProperty("http.agent"));
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        mWebSettings.setDomStorageEnabled(true);//DOM Storage
        mWebSettings.setBlockNetworkImage(false);
        webView.setWebViewClient(new PublicWebViewClient());
        getDetail(goods_id);
    }

    private void getDetail(String goods_id) {

        ApiMethodFactory.getInstance().getGoodInfo(goods_id, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<GoodsMallVo> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<GoodsMallVo>>() {
                });
                if (obj.getCode() == 200) {
                    for (int i = 0; i < obj.getData().getImageList().size(); i++) {
                        list.add(obj.getData().getImageList().get(i).getPhoto());
                    }
                    banner.setImages(list);
                    banner.start();
                    title.setText(obj.getData().getInfo().getTitle());
                    mallPrice.setText(Util.decimalFormatMoney(obj.getData().getInfo().getMallPrice()));
                    Spannable spannable = Spans.builder().text("原价:" + Util.decimalFormatMoney(obj.getData().getInfo().getPrice())).deleteLine().build();
                    price.setText(spannable);
                    number.setText("剩余" + obj.getData().getInfo().getNum() + obj.getData().getInfo().getGuige());
                    webView.loadDataWithBaseURL(null, getHtmlData(obj.getData().getInfo().getDetails()), "text/html", "UTF-8", null);

                    buy.setOnClickListener(v -> {
                        GoodsCartVo cartVo = new GoodsCartVo();
                        cartVo.setGoods_id(obj.getData().getInfo().getGoodsId());
                        cartVo.setNum(1);
                        cartVo.setMall_price(obj.getData().getInfo().getMallPrice());
                        cartVo.setTitle(obj.getData().getInfo().getTitle());
                        cartVo.setPhoto(obj.getData().getInfo().getPhoto());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("selectedGoods", cartVo);
                        Intent intent = new Intent();
                        intent.setClass(GoodDetailActivity.this, SubmitOrderActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });
                }
            }
        });

    }

    public static final String getHtmlData(String bodyHTML) {
        String head = "<head> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> <style>img{max-width: 100%; width:100%; height:auto;}</style> </head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

}
