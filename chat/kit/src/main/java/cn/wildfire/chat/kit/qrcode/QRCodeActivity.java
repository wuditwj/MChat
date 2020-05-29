package cn.wildfire.chat.kit.qrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.Result;
import com.hjq.toast.ToastUtils;
import com.king.zxing.util.CodeUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import cn.wildfire.chat.app.main.PCLoginActivity;
import cn.wildfire.chat.app.personalcenter.personalInformationActivity.UserInfoActivity;
import cn.wildfire.chat.app.personalcenter.utils.ImageUtil;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.channel.ChannelInfoActivity;
import cn.wildfire.chat.kit.conversation.forward.ForwardActivity;
import cn.wildfire.chat.kit.group.GroupInfoActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.chat.BuildConfig;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.ImageMessageContent;
import cn.wildfirechat.message.Message;
import cn.wildfirechat.message.MessageContentMediaType;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 我的二维码
 */
public class QRCodeActivity extends WfcBaseActivity {
    private String title;
    private String logoUrl;
    private String logoName;
    private String logoNoti;
    private String qrCodeValue;

    @BindView(R.id.qrCodeImageView)
    ImageView qrCodeImageView;

    @BindView(R.id.img_me)
    CircleImageView logoImg;

    @BindView(R.id.txt_name)
    TextView txtMyName;

    @BindView(R.id.txt_noti)
    TextView txtNoti;

    @BindView(R.id.btn_qr_share)
    Button btnShareMyQrCode;

    @BindView(R.id.rootView)
    LinearLayout rootView;

    private PromptDialog promptDialog;

    public static Intent buildQRCodeIntent(Context context, String title, String logoUrl, String qrCodeValue) {
        Intent intent = new Intent(context, QRCodeActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("logoUrl", logoUrl);
        intent.putExtra("qrCodeValue", qrCodeValue);
        return intent;
    }

    public static Intent buildQRCodeIntent(Context context, String title, String logoUrl, String logoName, String logoNoti, String qrCodeValue) {
        Intent intent = new Intent(context, QRCodeActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("logoUrl", logoUrl);
        intent.putExtra("qrCodeValue", qrCodeValue);
        intent.putExtra("logoName", logoName);
        intent.putExtra("logoNoti", logoNoti);
        return intent;
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        qrCodeValue = intent.getStringExtra("qrCodeValue");
        logoUrl = intent.getStringExtra("logoUrl");
        logoName = intent.getStringExtra("logoName");
        logoNoti = intent.getStringExtra("logoNoti");
    }

    private void showMenu() {
        rootView.buildDrawingCache();
        Bitmap orignal = rootView.getDrawingCache();
        String value = qrCodeValue.substring(qrCodeValue.lastIndexOf("/") + 1);
        File file = ImageUtil.saveFile(orignal, "/share" + value + ".png");//源文件
        String path = Environment.getExternalStorageDirectory() + "/AAA" + "/share" + value + ".png";
        File imageFileThumb = ImageUtils.genThumbImgFile(path);//获取缩略图路径
        Uri imageFileThumbUri = Uri.fromFile(imageFileThumb);//缩略图uri
        Uri imageFileSourceUri = Uri.fromFile(file);//源文件
        rootView.destroyDrawingCache();
        PromptButton cancle = new PromptButton("取消", null);
        cancle.setTextColor(Color.parseColor("#0076ff"));
        //设置显示的文字大小及颜色
        promptDialog.getAlertDefaultBuilder().textSize(12).textColor(ContextCompat.getColor(this, R.color.blue0));
        //默认两个按钮为Alert对话框，大于三个按钮的为底部SHeet形式展现
        promptDialog.showAlertSheet("", true, cancle,
                new PromptButton("转发", button -> {
                    Intent intent = new Intent(this, ForwardActivity.class);
                    Message message = new Message();
                    ImageMessageContent imageMessageContent = new ImageMessageContent(imageFileSourceUri.getEncodedPath());
                    imageMessageContent.setThumbnail(BitmapFactory.decodeFile(imageFileThumbUri.getEncodedPath()));
                    imageMessageContent.mediaType = MessageContentMediaType.IMAGE;
                    message.content = imageMessageContent;
                    intent.putExtra("message", message);
                    startActivity(intent);
                }),
                new PromptButton("识别二维码", button -> {
                    InputStream is = null;
                    try {

                        is = new FileInputStream(path);
                        Bitmap bmpSource = BitmapFactory.decodeStream(is);
                        Result result = QRCodeHelper.decodeQR(bmpSource);
                        onScanPcQrCode(result.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.show("当前图片二维码错误");
                    }

                }), new PromptButton("保存", button -> {
                    ToastUtils.show("图片已保存到" + file.getPath() + "下");

                })
        );
    }

    private void onScanPcQrCode(String qrcode) {
        String prefix = qrcode.substring(0, qrcode.lastIndexOf('/') + 1);
        String value = qrcode.substring(qrcode.lastIndexOf("/") + 1);
//        Uri uri = Uri.parse(value);
//        uri.getAuthority();
//        uri.getQuery()
        switch (prefix) {
            case WfcScheme.QR_CODE_PREFIX_PC_SESSION:
                pcLogin(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_USER:
                showUser(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_GROUP:
                joinGroup(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_CHANNEL:
                subscribeChannel(value);
                break;
            default:
                Toast.makeText(this, "qrcode: " + qrcode, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected int contentLayout() {
        return R.layout.qrcode_activity;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    protected void afterViews() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        View actionView = getLayoutInflater().inflate(R.layout.winter_abs_layout, null);
        TextView title = actionView.findViewById(R.id.tvTitle);
        title.setText("我的二维码");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        getSupportActionBar().setCustomView(actionView, params);
        //end
        btnShareMyQrCode.setOnClickListener(view -> {
            Bitmap bitmap = getBitmapFromView(qrCodeImageView);
//            try {
            File file = new File(getApplication().getExternalCacheDir(), "logicchip.png");
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();

                File path = getFilesDir();
                File pngFile = new File(path, "logicchip.png");
                Uri ImageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //String strpa = getApplicationContext().getPackageName();
                    ImageUri = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider", file);
                } else {
                    ImageUri = Uri.fromFile(file);
                }

//                file.setReadable(true, false);
                final Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_SEND);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, ImageUri);
                intent.setType("image/png");
                startActivity(Intent.createChooser(intent, "我的二维码"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        txtMyName.setText(logoName);
        txtNoti.setText(logoNoti);
        Glide.with(this).load(logoUrl).into(logoImg);
        promptDialog = new PromptDialog(this);
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMenu();
                return false;
            }
        });

        genQRCode();
    }

    private void subscribeChannel(String channelId) {
        Intent intent = new Intent(this, ChannelInfoActivity.class);
        intent.putExtra("channelId", channelId);
        startActivity(intent);
    }

    private void joinGroup(String groupId) {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    private void showUser(String uid) {

        UserInfo userInfo =   ChatManager.Instance().getUserInfo(uid,true);

        if (userInfo == null) {
            return;
        }
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    private void pcLogin(String token) {
        Intent intent = new Intent(this, PCLoginActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }


    private void genQRCode() {
        GlideApp.with(this)
                .asBitmap()
                .load(logoUrl)
                .placeholder(R.mipmap.ic_logo)
                .into(new CustomViewTarget<ImageView, Bitmap>(qrCodeImageView) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // the errorDrawable will always be bitmapDrawable here
                        if (errorDrawable instanceof BitmapDrawable) {
                            Bitmap bitmap = ((BitmapDrawable) errorDrawable).getBitmap();
                            Bitmap qrBitmap = CodeUtils.createQRCode(qrCodeValue, 400, bitmap);
                            qrCodeImageView.setImageBitmap(qrBitmap);
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition transition) {
                        Bitmap bitmap = CodeUtils.createQRCode(qrCodeValue, 400, resource);
                        qrCodeImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
