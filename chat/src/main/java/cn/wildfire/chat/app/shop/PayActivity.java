package cn.wildfire.chat.app.shop;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.alipay.AliPayHandler;
import cn.wildfire.chat.app.alipay.AliPayUtils;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.adapter.GoodsMallAdapter;
import cn.wildfire.chat.app.shop.adapter.PayAdapter;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.shop.entity.GoodsCartVo;
import cn.wildfire.chat.app.shop.entity.OrderDetailInfo;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 订单提交页面
 */
public class PayActivity extends BaseActivity {

    @BindView(R.id.choose_address)
    LinearLayout chooseAddress;
    @BindView(R.id.goods_list)
    RecyclerView goodsList;//商品列表
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.pay_list)
    RecyclerView payList;//支付方式列表
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.address_name)
    TextView addressName;
    @BindView(R.id.address_number)
    TextView addressNumber;
    @BindView(R.id.address)
    TextView address;

    private String order_id;
    private AliPayHandler aliPayHandler;


    @Override
    protected int contentLayout() {
        return R.layout.activity_pay;
    }

    protected void init() {
        toolbarTitle.setText("填写订单信息");
        order_id = getIntent().getStringExtra("order_id");
        payList.setLayoutManager(new LinearLayoutManager(this));
        payList.addItemDecoration(new ItemDecorationDivider(this,
                ItemDecorationDivider.VERTICAL_LIST, 1,
                ContextCompat.getColor(this, R.color.line)));
        goodsList.setLayoutManager(new LinearLayoutManager(this));
        goodsList.addItemDecoration(new ItemDecorationDivider(this,
                ItemDecorationDivider.VERTICAL_LIST, 1, ContextCompat.getColor(this, R.color.white)));
        getOrderDetail(order_id);
        final PayAdapter payAdapter = new PayAdapter(R.layout.item_pay, Util.getPay());
        payList.setAdapter(payAdapter);
        aliPayHandler = new AliPayHandler() {
            @Override
            protected void onSuccess() {
                //调到订单列表
                Intent intent = new Intent();
                intent.setClass(PayActivity.this,
                        OrderListActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            protected void onChecking() {
            }

            @Override
            protected void onFail() {
                ToastUtils.show("支付失败");
            }

        };

    }

    /**
     * 获取订单信息
     *
     * @param order_id
     */
    private void getOrderDetail(String order_id) {
        ApiMethodFactory.getInstance().getOrderDetail(order_id, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<OrderDetailInfo> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<OrderDetailInfo>>() {
                });
                if (obj.getCode() == 200) {
                    addressName.setText(obj.getData().getName());
                    addressNumber.setText(obj.getData().getMobile());
                    address.setText(obj.getData().getAddr());

                    List<GoodsCartVo> goodsCartVos = new ArrayList<>();
                    GoodsCartVo cartVo = new GoodsCartVo();
                    cartVo.setTitle(obj.getData().getTitle());
                    cartVo.setPhoto(obj.getData().getPhoto());
                    cartVo.setMall_price(obj.getData().getPrice());
                    cartVo.setNum(obj.getData().getNum());
                    goodsCartVos.add(cartVo);
                    GoodsMallAdapter cartAdapter = new GoodsMallAdapter(R.layout.item_mall_goods_list, goodsCartVos);
                    goodsList.setAdapter(cartAdapter);
                }
            }
        });
    }

    /**
     * 支付
     *
     * @param type
     */
    private void onPay(String type) {
        ApiMethodFactory.getInstance().onPay(order_id, type, ChatManager.Instance().getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                switch (type) {
                    case "1":
                        BaseBean<String> aliPay = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                        });
                        AliPayUtils aliPayUtils = new AliPayUtils(PayActivity.this, aliPayHandler);
                        if (aliPay.getCode() == 200) {
                            aliPayUtils.pay(aliPay.getData());

                        }
                        break;
                    case "2":

                        break;

                }
            }
        });
    }

    @OnClick({R.id.choose_address, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_address:
                break;
            case R.id.submit:
                onPay("1");
                break;
        }
    }
}
