package cn.wildfire.chat.app.shop;

import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hjq.toast.ToastUtils;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.zrq.spanbuilder.Spans;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.adapter.GoodsMallAdapter;
import cn.wildfire.chat.app.shop.entity.AddressInfo;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.shop.entity.GoodsCartVo;
import cn.wildfire.chat.app.shop.entity.MallSubmitVo;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 立即购买填写订单信息
 */
public class SubmitOrderActivity extends BaseActivity {

    @BindView(R.id.choose_address)
    LinearLayout chooseAddress;
    @BindView(R.id.goods_list)
    RecyclerView goodsList;
    @BindView(R.id.beizhu)
    EditText beizhu;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.address_name)
    TextView addressName;
    @BindView(R.id.address_number)
    TextView addressNumber;
    @BindView(R.id.address)
    TextView address;

    private GoodsCartVo selectedGoods;
    private String addressId = "";

    @Override
    protected int contentLayout() {
        return R.layout.activity_submit_order;
    }

    protected void init() {
        toolbarTitle.setText("填写订单信息");
        selectedGoods = getIntent().getParcelableExtra("selectedGoods");
        goodsList.setLayoutManager(new LinearLayoutManager(this));
        goodsList.addItemDecoration(new ItemDecorationDivider(this,
                ItemDecorationDivider.VERTICAL_LIST, 1, ContextCompat.getColor(this, R.color.white)));
        List<GoodsCartVo> goodsCartVos = new ArrayList<>();
        goodsCartVos.add(selectedGoods);
        GoodsMallAdapter cartAdapter = new GoodsMallAdapter(R.layout.item_mall_goods_list, goodsCartVos);
        goodsList.setAdapter(cartAdapter);
        double allPrice = selectedGoods.getMall_price() * selectedGoods.getNum();

        Spannable spannable = Spans.builder().text("合计:", 17,
                ContextCompat.getColor(this, R.color.red6))
                .text(Util.decimalFormatMoney(allPrice), 17, ContextCompat.getColor(this, R.color.red6)).build();
        price.setText(spannable);
        chooseAddress.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, AddressListActivity.class);
            startActivity(intent);
        });
        submit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(addressId)) {
                ToastUtils.show("请选择收货地址");
                return;
            }
            MallSubmitVo mallSubmitVo = new MallSubmitVo();
            mallSubmitVo.setAddressId(addressId);
            mallSubmitVo.setGoods_id(selectedGoods.getGoods_id());
            mallSubmitVo.setBeizhu(beizhu.getText().toString().trim());
            mallSubmitVo.setUser_id(ChatManager.Instance().getUserId());
            mallSubmitVo.setGoodsCartVos(goodsCartVos);
            mallSubmitVo.setNum(1);
            submit(JSONObject.toJSONString(mallSubmitVo));
        });

    }

    @BusReceiver
    public void onMainThread(AddressInfo event) {
        addressId = event.getAddrId();
        addressName.setVisibility(View.VISIBLE);
        addressNumber.setVisibility(View.VISIBLE);
        addressName.setText(event.getName());
        addressNumber.setText(event.getMobile());
        address.setText(event.getAreaName() + event.getAddr());
    }

    private void submit(String json) {
        ApiMethodFactory.getInstance().submit(json, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    Intent intent = new Intent();
                    intent.setClass(SubmitOrderActivity.this, PayActivity.class);
                    intent.putExtra("order_id", obj.getData());
                    startActivity(intent);
                }
            }
        });
    }
}
