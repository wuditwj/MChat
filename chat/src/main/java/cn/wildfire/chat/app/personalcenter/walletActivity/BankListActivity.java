package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.mcxiaoke.bus.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.adapter.BankCardAdapter;
import cn.wildfire.chat.app.personalcenter.entity.CardVo;
import cn.wildfire.chat.app.shop.utils.GridSpacingItemDecoration;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfirechat.chat.R;

/**
 * 我的银行卡
 */
public class BankListActivity extends BaseActivity {

    @BindView(R.id.card_list)
    RecyclerView cardList;
    @BindView(R.id.add_card)
    RadiusTextView addCard;

    private BankCardAdapter bankCardAdapter;
    private boolean choose;

    @Override
    protected int contentLayout() {
        return R.layout.activity_bank_list;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("我的银行卡");

        choose = getIntent().getBooleanExtra("choose", false);

        bankCardAdapter = new BankCardAdapter(R.layout.item_bank, new ArrayList<>());
        cardList.setLayoutManager(new LinearLayoutManager(this));
        cardList.addItemDecoration(new GridSpacingItemDecoration(1, UIUtils.dip2Px(10), true));
        cardList.setAdapter(bankCardAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApiMethodFactory.getInstance().getUserBankList(ChatManagerHolder.gChatManager.getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<List<CardVo>> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<List<CardVo>>>() {
                });
                if (obj.getCode() == 200) {
                    bankCardAdapter.setNewData(obj.getData());
                    bankCardAdapter.setOnItemClickListener((adapter, view, position) -> {
                        if (choose) {
                            Bus.getDefault().post(obj.getData().get(position));
                            finish();
                        } else {
                            Intent intent = new Intent(BankListActivity.this, UserBankActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("cardVo", obj.getData().get(position));
                            intent.putExtra("position", position);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }


                    });

                } else {
                    bankCardAdapter.setNewData(new ArrayList<>());
                }
            }
        });
    }

    @OnClick(R.id.add_card)
    public void onViewClicked() {
        startActivity(new Intent(BankListActivity.this, AddBank.class));
    }
}
