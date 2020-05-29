package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.entity.CardVo;
import cn.wildfire.chat.app.shop.utils.Util;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfirechat.chat.R;

/**
 * 我的银行卡/解绑
 */
public class UserBankActivity extends BaseActivity {

    @BindView(R.id.bank_logo)
    ImageView bankLogo;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.card_no)
    TextView cardNo;
    @BindView(R.id.bg_root)
    RadiusRelativeLayout bgRoot;
    @BindView(R.id.un_bind_Card)
    RadiusTextView unBindCard;

    private CardVo cardVo;
    private int position;

    @Override
    protected int contentLayout() {
        return R.layout.activity_user_bank;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("我的银行卡");
        cardVo = getIntent().getParcelableExtra("cardVo");
        bankName.setText(cardVo.getBank());
        cardNo.setText(Util.replace(Util.hideCardNo(cardVo.getBank_account())));
        GlideApp.with(this).load(cardVo.getBank_logo())
                .into(bankLogo);
        position = getIntent().getIntExtra("position", -1);
    }

    @OnClick(R.id.un_bind_Card)
    public void onViewClicked() {
        deleteCard(cardVo.getId());
    }

    private void deleteCard(String id) {
        ApiMethodFactory.getInstance().delUserBank(id, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    finish();
                }
                ToastUtils.show(obj.getMessage());
            }
        });
    }
}
