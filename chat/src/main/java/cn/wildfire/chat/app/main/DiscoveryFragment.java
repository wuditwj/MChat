package cn.wildfire.chat.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.shop.OrderListActivity;
import cn.wildfire.chat.app.shop.adapter.GoodsAdapter;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.shop.entity.BaseTotalPage;
import cn.wildfire.chat.app.shop.entity.GoodsListInfo;
import cn.wildfire.chat.app.shop.utils.GridSpacingItemDecoration;
import cn.wildfirechat.chat.R;

/**
 * 萌聊商城页面
 */
public class DiscoveryFragment extends Fragment {

    @BindView(R.id.toolbar_right)
    TextView toolbarRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.goods_list)
    RecyclerView goodsList;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.toolBar_top)
    View toolBarTop;
    private GoodsAdapter goodsAdapter;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_discovery, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        ImmersionBar.with(getActivity()).statusBarView(toolBarTop).init();
        goodsAdapter = new GoodsAdapter(getContext(), new ArrayList<>());
        goodsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        goodsList.addItemDecoration(new GridSpacingItemDecoration(2, 20, true));
        goodsList.setAdapter(goodsAdapter);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getGoodsList(page);
            refreshLayout.finishRefresh();
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getGoodsList(page);
                refreshLayout.finishLoadMore();
            }
        });

        getGoodsList(page);
    }

    private void getGoodsList(int page) {

        ApiMethodFactory.getInstance().getGoods(page, 10, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<BaseTotalPage<GoodsListInfo>> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<BaseTotalPage<GoodsListInfo>>>() {
                });
                if (obj.getCode()==200){
                    //第一页
                    if (page==1){
                        goodsAdapter.setNewData(obj.getData().getOrderList());
                    }else{
                        //上拉加载
                        if(page<obj.getData().getTotalPage()){
                            goodsAdapter.addNewData(obj.getData().getOrderList());
                        }else{
                            //最后一页
                            smartRefreshLayout.setNoMoreData(true);
                        }
                    }
                }
            }
        });
    }

    /**
     * 我的订单
     */
    @OnClick(R.id.toolbar_right)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), OrderListActivity.class));
    }
}
