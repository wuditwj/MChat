package cn.wildfire.chat.app.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;
import com.mcxiaoke.bus.Bus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.adapter.AddressAdapter;
import cn.wildfire.chat.app.shop.entity.AddressInfo;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.shop.utils.GridSpacingItemDecoration;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 收货地址设置
 */
public class AddressListActivity extends BaseActivity {
    @BindView(R.id.address_list)
    RecyclerView addressList;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.add_address)
    RadiusTextView addAddress;

    private AddressAdapter addressAdapter;

    @Override
    protected int contentLayout() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    protected void init() {
        toolbarTitle.setText("收货地址设置");
        addressList.setLayoutManager(new LinearLayoutManager(this));
        addressList.addItemDecoration(new GridSpacingItemDecoration(1, 20, false));
        addressAdapter = new AddressAdapter(R.layout.item_address, new ArrayList<>());
        addressList.setAdapter(addressAdapter);
        //添加地址
        addAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this,EditorAddressActivity.class);
            startActivity(intent);

        });
        smartLayout.setOnRefreshListener(refreshLayout -> {
            getAddress();
            refreshLayout.finishRefresh();
        });
    }

    /**
     * 获取地址集合
     */
    private void getAddress() {
        ApiMethodFactory.getInstance().getAddress(ChatManager.Instance().getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<List<AddressInfo>> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<List<AddressInfo>>>() {
                });
                if (obj.getCode() == 200) {
                    addressAdapter.setList(obj.getData());
                    addressAdapter.notifyDataSetChanged();
                    addressAdapter.setOnItemClickListener((adapter, view, position) -> {
                        Bus.getDefault().post(addressAdapter.getData().get(position));
                        finish();
                    });
                    addressAdapter.addChildClickViewIds(R.id.delete,R.id.editor);
                    addressAdapter.setOnItemChildClickListener((adapter, view, position) -> {

                        switch (view.getId()) {
                            case R.id.delete:
                                onDelete(addressAdapter.getData().get(position).getAddrId());
                                break;
                            case R.id.editor:
                                //编辑地址
                                Intent intent = new Intent(AddressListActivity.this,EditorAddressActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putParcelable("addressInfo",addressAdapter.getData().get(position));
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                        }
                    });
                }
            }
        });
    }

    /**
     * 删除地址
     *
     * @param addressId
     */
    private void onDelete(String addressId) {
        ApiMethodFactory.getInstance().delAddress(addressId, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    getAddress();
                }
                ToastUtils.show(obj.getMessage());
            }
        });
    }
}
