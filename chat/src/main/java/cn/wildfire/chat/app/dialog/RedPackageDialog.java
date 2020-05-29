package cn.wildfire.chat.app.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.CircleImageView;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class RedPackageDialog extends Dialog {
    @BindView(R.id.header_img)
    CircleImageView headerImg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.reason)
    TextView reason;
    @BindView(R.id.open)
    ImageView open;
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.open_dialog)
    RelativeLayout openDialog;
    onClickRedPackageListener handler;
    private int code;
    private String redTitle;
    private String send_user_id;

    public interface onClickRedPackageListener {
        public void onClick();
    }

    public RedPackageDialog(Context context, int code, String redTitle, String send_user_id, onClickRedPackageListener handler) {
        super(context, R.style.dialog_select_gender);
        this.handler = handler;
        this.code = code;
        this.redTitle = redTitle;
        this.send_user_id = send_user_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        setContentView(R.layout.dialog_red_package);
        ButterKnife.bind(this);
//        RelativeLayout imageView = v.findViewById(R.id.open_dialog);
//        ImageView portrait = v.findViewById(R.id.header_img);
//        TextView name = v.findViewById(R.id.name);
//        TextView detail = v.findViewById(R.id.detail);
        if (!TextUtils.isEmpty(send_user_id)) {
            if (TextUtils.equals(send_user_id, ChatManager.Instance().getUserId())) {
                detail.setVisibility(View.GONE);
            }
            UserInfo userInfo = ChatManagerHolder.gChatManager.getUserInfo(send_user_id, false);
            Glide.with(getContext()).load(userInfo.portrait).into(headerImg);
            name.setText(userInfo.displayName);
        }


//        ImageView open = v.findViewById(R.id.open);
//        TextView reason = v.findViewById(R.id.reason);
//
//        TextView content = v.findViewById(R.id.content);
        content.setText(redTitle);
        switch (code) {
            case 200:
                reason.setVisibility(View.GONE);
                open.setVisibility(View.VISIBLE);
                break;
            case 600:
                reason.setVisibility(View.VISIBLE);
                reason.setText("红包已过期");
                open.setVisibility(View.GONE);

                break;
            case 700:
                reason.setVisibility(View.VISIBLE);
                reason.setText("手慢了,红包派完了");
                open.setVisibility(View.GONE);
                break;

        }
        openDialog.setOnClickListener(v1 -> {
            //打开红包操作
            handler.onClick();
        });
        setCanceledOnTouchOutside(true);
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int windowWidth = dm.widthPixels;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = windowWidth / 6 * 5;
        getWindow().setAttributes(lp);
    }
}
