package cn.wildfire.chat.app.personalcenter.setting;

import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.SuperTextView;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.entity.SendRedVo;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

public class SetPwd extends BaseActivity {

    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.pwd_1)
    EditText pwd1;
    @BindView(R.id.submit)
    SuperTextView submit;

    @Override
    protected int contentLayout() {
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("设置密码");
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        ApiMethodFactory.getInstance().updatePwd(ChatManager.Instance().getUserId(), pwd.getText().toString().trim(), pwd1.getText().toString().trim(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<SendRedVo> baseBean = JSONObject.parseObject(response, new TypeReference<BaseBean<SendRedVo>>() {
                });
                if (baseBean.getCode() == 200) {
                    finish();
                }
                ToastUtils.show(baseBean.getMessage());
            }
        });
    }
}
