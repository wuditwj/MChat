package cn.wildfire.chat.app.envelope;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

//import cn.wildfire.chat.app.wallet.WalletPayCode;
import cn.wildfire.chat.app.widget.PayPwdView;
import cn.wildfire.chat.app.widget.PwdInputMethodView;
import cn.wildfirechat.chat.R;


public class PayFragment extends DialogFragment implements View.OnClickListener {

    public static final String EXTRA_CONTENT = "extra_content";    //提示框内容

    private PayPwdView psw_input;
    private PayPwdView.InputCallBack inputCallBack;
    private TextView forget_plus_card_password;
    private String pay;

    public PayPwdView getPsw_input() {
        return psw_input;
    }

    public void setPsw_input(PayPwdView psw_input) {
        this.psw_input = psw_input;
    }

    public static PayFragment newInstance(String pay) {
        final PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();
        args.putString("pay", pay);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_pay);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

// 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.gravity = Gravity.TOP;
        window.setAttributes(lp);

        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            TextView tv_content = dialog.findViewById(R.id.tv_content);
            tv_content.setText(bundle.getString(EXTRA_CONTENT));
            pay = bundle.getString("pay");
            tv_content.setText("￥"+pay);
        }
        psw_input = dialog.findViewById(R.id.payPwdView);
        PwdInputMethodView inputMethodView = dialog.findViewById(R.id.inputMethodView);
        psw_input.setInputMethodView(inputMethodView, false);
        psw_input.setInputCallBack(inputCallBack);
        forget_plus_card_password = dialog.findViewById(R.id.forget_plus_card_password);
        forget_plus_card_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), WalletPayCode.class));忘记密码
            }
        });
        dialog.findViewById(R.id.iv_close).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;

        }
    }

    /**
     * 设置输入回调
     *
     * @param inputCallBack
     */
    public void setPaySuccessCallBack(PayPwdView.InputCallBack inputCallBack) {
        this.inputCallBack = inputCallBack;
    }
}
