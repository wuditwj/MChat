package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.content.Intent;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.entity.BankVo;
import cn.wildfirechat.chat.R;

/**
 * 添加银行卡
 */
public class AddBank extends BaseActivity {

    @BindView(R.id.bank)
    EditText bank;
    @BindView(R.id.accountOpening)
    EditText accountOpening;
    @BindView(R.id.bankAccount)
    EditText bankAccount;
    @BindView(R.id.branch)
    EditText branch;
    @BindView(R.id.submit)
    RadiusTextView submit;

    @Override
    protected int contentLayout() {
        return R.layout.activity_add_bank;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("添加银行卡");
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {

        ApiMethodFactory.getInstance().checkBankType(bankAccount.getText().toString().trim(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                try {
                    BaseBean<BankVo> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<BankVo>>() {
                    });
                    if (obj.getCode() == 200) {
                        Intent intent = new Intent(AddBank.this, AddBankAccountActivity.class);
                        intent.putExtra("bankName", obj.getData().getBank());
                        intent.putExtra("bankLogo", obj.getData().getLogo());
                        intent.putExtra("bankNo", bankAccount.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {

                }
            }
        });
    }
}
