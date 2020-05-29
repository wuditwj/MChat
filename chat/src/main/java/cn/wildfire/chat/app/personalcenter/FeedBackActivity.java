package cn.wildfire.chat.app.personalcenter;


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
import cn.wildfire.chat.app.personalcenter.utils.ProgressDialogUtil;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.chat.R;
import fj.edittextcount.lib.FJEditTextCount;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.fjEdit)
    FJEditTextCount fjEdit;
    @BindView(R.id.submit)
    RadiusTextView submit;

    @Override
    protected int contentLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("意见反馈");
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {

        ApiMethodFactory.getInstance().addOpinion(ChatManagerHolder.gChatManager.getUserId(), fjEdit.getText().trim(), new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                });
                if (obj.getCode() == 200) {
                    finish();
                }
                ToastUtils.show(obj.getMessage());
                ProgressDialogUtil.dismiss(FeedBackActivity.this);
            }
        });
    }
}
