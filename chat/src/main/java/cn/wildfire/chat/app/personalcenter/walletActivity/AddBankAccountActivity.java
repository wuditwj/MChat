package cn.wildfire.chat.app.personalcenter.walletActivity;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.entity.BankVo;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.chat.R;

/**
 * 添加银行卡
 */
public class AddBankAccountActivity extends BaseActivity {

    @BindView(R.id.bankName)
    TextView tvBankName;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.id_card_no)
    EditText idCardNo;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.submit)
    RadiusTextView submit;

    private String bankName, bankNo, bankLogo;

    @Override
    protected int contentLayout() {
        return R.layout.activity_add_bank_account;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("添加银行卡");
        bankName = getIntent().getStringExtra("bankName");
        bankNo = getIntent().getStringExtra("bankNo");
        bankLogo = getIntent().getStringExtra("bankLogo");
        tvBankName.setText(bankName);
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        ApiMethodFactory.getInstance().addUserBank(ChatManagerHolder.gChatManager.getUserId(),
                name.getText().toString().trim(),
                bankName,
                bankNo,
                "222",
                name.getText().toString().trim(),
                idCardNo.getText().toString().trim(),
                mobile.getText().toString().trim(),
                bankLogo,
                new HttpHandler() {
                    @Override
                    public void requestSuccess(String response) {
                        try {
                            JSONObject result = JSONObject.parseObject(response);
                            if (result.get("code").toString().equals("400")) {
                                Toast.makeText(AddBankAccountActivity.this, result.get("data").toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                            BaseBean<BankVo> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<BankVo>>() {
                            });
                            if (obj.getCode() == 200) {
                                finish();
                            }
                            ToastUtils.show(obj.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
}
