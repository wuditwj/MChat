package cn.wildfire.chat.app.personalcenter;

import android.widget.CompoundButton;

import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfirechat.chat.R;

/**
 * 新消息通知
 */
public class NewMessageActivity extends BaseActivity {

    @BindView(R.id.get_new_message)
    SwitchButton getNewMessage;
    @BindView(R.id.show_message)
    SwitchButton showMessage;

    @Override
    protected int contentLayout() {
        return R.layout.activity_new_message;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("新消息通知");

        getNewMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        showMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

}
