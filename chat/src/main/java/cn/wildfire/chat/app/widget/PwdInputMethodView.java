package cn.wildfire.chat.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import cn.wildfirechat.chat.R;


/**
 * Created by Administrator on 2018\1\16 0016.
 */

public class PwdInputMethodView extends LinearLayout implements View.OnClickListener  {
    private InputReceiver inputReceiver;

    public PwdInputMethodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_password_input, this);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_del).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String num = (String) v.getTag();
        this.inputReceiver.receive(num);
    }


    /**
     * 设置接收器
     * @param receiver
     */
    public void setInputReceiver(InputReceiver receiver){
        this.inputReceiver = receiver;
    }

    /**
     * 输入接收器
     */
    public interface InputReceiver{

        void receive(String num);
    }
}