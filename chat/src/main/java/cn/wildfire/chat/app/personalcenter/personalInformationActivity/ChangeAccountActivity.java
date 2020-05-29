package cn.wildfire.chat.app.personalcenter.personalInformationActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.ModifyMyInfoEntry;
import cn.wildfirechat.model.ModifyMyInfoType;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.remote.GeneralCallback;

/**
 * 修改萌聊号
 */
public class ChangeAccountActivity extends BaseActivity {

    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.toolbar_right)
    TextView toolbarRight;

    @Override
    protected int contentLayout() {
        return R.layout.activity_change_account;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("萌聊号");

        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c >= 0x4e00 && c <= 0X9fff) { // 根据字节码判断
                            // 如果是中文，则清除输入的字符，否则保留
                            s.delete(i, i + 1);
                        }
                    }
                }
            }
        });
    }


    @OnClick(R.id.toolbar_right)
    public void onViewClicked() {
        //保存
        setAccount(account.getText().toString().trim());
    }

    private void setAccount(String account) {

        List<ModifyMyInfoEntry> entries = new ArrayList<>();
        entries.add(new ModifyMyInfoEntry(ModifyMyInfoType.Modify_Email, account));
        ChatManager.Instance().modifyMyInfo(entries, new GeneralCallback() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onFail(int errorCode) {
            }
        });
    }
}
