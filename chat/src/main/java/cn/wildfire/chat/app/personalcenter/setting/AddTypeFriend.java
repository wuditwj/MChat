package cn.wildfire.chat.app.personalcenter.setting;

import android.text.TextUtils;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.entity.AccountState;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.chat.R;

/**
 * 添加我的方式
 */
public class AddTypeFriend extends BaseActivity {

    @BindView(R.id.mobile)
    SwitchButton mobile;
    @BindView(R.id.account)
    SwitchButton account;

    @Override
    protected int contentLayout() {
        return R.layout.activity_add_type_friend;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("通用");
        getStatus();
        mobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String type = isChecked ? "2" : "1";
                setType(type, "/userManage/updateMobileStatus");

            }
        });
        account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String type = isChecked ? "2" : "1";
                setType(type, "/userManage/updateSocialStatus");

            }
        });
    }

    private void setType(String type, String urlPath) {

        ApiMethodFactory.getInstance().setType(ChatManagerHolder.gChatManager.getUserId(), type, urlPath, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {

            }
        });
    }

    private void getStatus() {
        ApiMethodFactory.getInstance().serachStatus(ChatManagerHolder.gChatManager.getUserId(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<AccountState> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<AccountState>>() {
                });
                if (obj.getCode() == 200) {
                    mobile.setChecked(TextUtils.equals("2", obj.getData().getMobileStatus()));
                    account.setChecked(TextUtils.equals("2", obj.getData().getSocialStatus()));
                }
            }
        });
    }
}
