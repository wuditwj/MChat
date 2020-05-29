package cn.wildfire.chat.kit.mm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.hjq.toast.ToastUtils;
import com.king.zxing.util.CodeUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.main.PCLoginActivity;
import cn.wildfire.chat.app.personalcenter.utils.ImageUtil;
import cn.wildfire.chat.app.util.ImageSaveUtils;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.channel.ChannelInfoActivity;
import cn.wildfire.chat.kit.conversation.forward.ForwardActivity;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfire.chat.kit.group.GroupInfoActivity;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfire.chat.kit.utils.DownloadManager;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.ImageMessageContent;
import cn.wildfirechat.message.Message;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * @author imndx
 */
public class MMPreviewActivity extends Activity {
    private SparseArray<View> views;
    private View currentVideoView;
    private ViewPager viewPager;
    private MMPagerAdapter adapter;

    private static int currentPosition = -1;
    private static List<MediaEntry> entries;
    private boolean pendingPreviewInitialMedia;
    private PromptDialog promptDialog;
    private static UiMessage uiMessage;

    private class MMPagerAdapter extends PagerAdapter {
        private List<MediaEntry> entries;

        public MMPagerAdapter(List<MediaEntry> entries) {
            this.entries = entries;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view;
            MediaEntry entry = entries.get(position);
            if (entry.getType() == MediaEntry.TYPE_IMAGE) {
                view = LayoutInflater.from(MMPreviewActivity.this).inflate(R.layout.preview_photo, null);
            } else {
                view = LayoutInflater.from(MMPreviewActivity.this).inflate(R.layout.preview_video, null);
            }

            container.addView(view);
            views.put(position % 3, view);
            if (pendingPreviewInitialMedia) {
                preview(view, entry);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            // do nothing ?
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return entries == null ? 0 : entries.size();
        }

        public MediaEntry getEntry(int position) {
            return entries.get(position);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO 可以在此控制透明度
        }

        @Override
        public void onPageSelected(int position) {
            View view = views.get(position % 3);
            if (view == null) {
                // pending layout
                return;
            }
            if (currentVideoView != null) {
                resetVideoView(currentVideoView);
                currentVideoView = null;
            }
            MediaEntry entry = adapter.getEntry(position);
            preview(view, entry);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void preview(View view, MediaEntry message) {
        if (message.getType() == MediaEntry.TYPE_IMAGE) {
            previewImage(view, message);
        } else {
            previewVideo(view, message);
        }
    }

    private void resetVideoView(View view) {
        PhotoView photoView = view.findViewById(R.id.photoView);
        ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
        ImageView playButton = view.findViewById(R.id.btnVideo);
        VideoView videoView = view.findViewById(R.id.videoView);

        photoView.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.GONE);
        playButton.setVisibility(View.VISIBLE);
        videoView.stopPlayback();
        videoView.setVisibility(View.INVISIBLE);
    }

    private void previewVideo(View view, MediaEntry entry) {

        PhotoView photoView = view.findViewById(R.id.photoView);
        ImageView saveImageView = view.findViewById(R.id.saveImageView);
        saveImageView.setVisibility(View.GONE);
        if (entry.getThumbnail() != null) {
            GlideApp.with(photoView).load(entry.getThumbnail()).into(photoView);
        } else {
            GlideApp.with(photoView).load(entry.getThumbnailUrl()).into(photoView);
        }

        VideoView videoView = view.findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);

        ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.GONE);

        ImageView btn = view.findViewById(R.id.btnVideo);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(entry.getMediaUrl())) {
                    return;
                }
                btn.setVisibility(View.GONE);
                if (TextUtils.isEmpty(entry.getMediaLocalPath())) {
                    String name = DownloadManager.md5(entry.getMediaUrl());
                    File videoFile = new File(Config.VIDEO_SAVE_DIR, name);
                    if (!videoFile.exists()) {
                        view.setTag(name);
                        ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        final WeakReference<View> viewWeakReference = new WeakReference<>(view);
                        DownloadManager.download(entry.getMediaUrl(), Config.VIDEO_SAVE_DIR, name, new DownloadManager.OnDownloadListener() {
                            @Override
                            public void onSuccess(File file) {
                                UIUtils.postTaskSafely(() -> {
                                    View targetView = viewWeakReference.get();
                                    if (targetView != null && name.equals(targetView.getTag())) {
                                        targetView.findViewById(R.id.loading).setVisibility(View.GONE);
                                        playVideo(targetView, file.getAbsolutePath());
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int progress) {
                                // TODO update progress
                                Log.e(MMPreviewActivity.class.getSimpleName(), "video downloading progress: " + progress);
                            }

                            @Override
                            public void onFail() {
                                View targetView = viewWeakReference.get();
                                UIUtils.postTaskSafely(() -> {
                                    if (targetView != null && name.equals(targetView.getTag())) {
                                        targetView.findViewById(R.id.loading).setVisibility(View.GONE);
                                        targetView.findViewById(R.id.btnVideo).setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                    } else {
                        playVideo(view, videoFile.getAbsolutePath());
                    }
                } else {
                    playVideo(view, entry.getMediaLocalPath());
                }
            }
        });
    }

    private void playVideo(View view, String videoUrl) {
        VideoView videoView = view.findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);

        PhotoView photoView = view.findViewById(R.id.photoView);
        photoView.setVisibility(View.GONE);

        ImageView btn = view.findViewById(R.id.btnVideo);
        btn.setVisibility(View.GONE);

        ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.GONE);
        view.findViewById(R.id.loading).setVisibility(View.GONE);
        currentVideoView = view;

        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoPath(videoUrl);
        videoView.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(MMPreviewActivity.this, "play error", Toast.LENGTH_SHORT).show();
            resetVideoView(view);
            return true;
        });
        videoView.setOnCompletionListener(mp -> resetVideoView(view));
        videoView.start();

    }

    private void previewImage(View view, MediaEntry entry) {
        PhotoView photoView = view.findViewById(R.id.photoView);
        ImageView saveImageView = view.findViewById(R.id.saveImageView);
        String mediaUrl = entry.getMediaUrl();
        photoView.setOnClickListener(v -> {
            //点击返回
            onBackPressed();
        });
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //长安识别二维码
                showMenu(entry, mediaUrl);
                return false;
            }
        });
//        if (TextUtils.isEmpty(entry.getMediaLocalPath()) && !TextUtils.isEmpty(mediaUrl)) {
//            String imageFileName = DownloadManager.md5(mediaUrl) + mediaUrl.substring(mediaUrl.lastIndexOf('.'));
//            File file = new File(Config.PHOTO_SAVE_DIR, imageFileName);
//            if (file.exists()) {
//                saveImageView.setVisibility(View.GONE);
//            } else {
//                saveImageView.setVisibility(View.VISIBLE);
//                saveImageView.setOnClickListener(v -> {
//                    saveImageView.setVisibility(View.GONE);
//                    DownloadManager.download(entry.getMediaUrl(), Config.PHOTO_SAVE_DIR, imageFileName, new DownloadManager.SimpleOnDownloadListener() {
//                        @Override
//                        public void onUiSuccess(File file1) {
//                            if (isFinishing()) {
//                                return;
//                            }
//                            Toast.makeText(MMPreviewActivity.this, "图片保存成功", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                });
//            }
//        } else {
//            saveImageView.setVisibility(View.GONE);
//        }

        if (entry.getThumbnail() != null) {
            GlideApp.with(MMPreviewActivity.this).load(entry.getMediaUrl())
                    .placeholder(new BitmapDrawable(getResources(), entry.getThumbnail()))
                    .into(photoView);
        } else {
            GlideApp.with(MMPreviewActivity.this).load(entry.getMediaUrl())
                    .placeholder(new BitmapDrawable(getResources(), entry.getThumbnailUrl()))
                    .into(photoView);
        }
    }

    private void showMenu(MediaEntry entry, String path) {
        PromptButton cancle = new PromptButton("取消", null);
        cancle.setTextColor(Color.parseColor("#0076ff"));
        //设置显示的文字大小及颜色
        promptDialog.getAlertDefaultBuilder().textSize(12).textColor(ContextCompat.getColor(this, R.color.blue0));

        //默认两个按钮为Alert对话框，大于三个按钮的为底部SHeet形式展现
        promptDialog.showAlertSheet("", true, cancle,

                new PromptButton("识别图中二维码", button -> {
                    Glide.with(this).asBitmap().load(path).into(new SimpleTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            File file = ImageUtil.saveFile(resource, "/" + 222 + ".png");//源文件
                            String BitMappath = file.getAbsolutePath();
                            final String result = CodeUtils.parseCode(BitMappath);
                            if (!TextUtils.isEmpty(result)) {
                                onScanPcQrCode(result);
                            } else {
                                ToastUtils.show("当前二维码异常");
                            }

                        }
                    });


//                    onScanPcQrCode(result);
//                    Result result = QRCodeHelper.decodeQR(ImageUtils.getLoacalBitmap(path));
//                    try {
//                        onScanPcQrCode(result.getText());//此处野火IM自带bug  IOS的二维码图（群聊类的二维码无法识别）
//                    }catch (Exception e){
//
//                    }


                }),
                new PromptButton("转发", button -> {
                    try {

                        Intent intent = new Intent(this, ForwardActivity.class);
                        intent.putExtra("message", uiMessage.message);
                        startActivity(intent);
                    } catch (Exception e) {
                        ToastUtils.show("图片格式问题,无法转发");
                    }

                }),
                new PromptButton("保存", button -> {
                    String mediaUrl = entry.getMediaUrl();
//                    InputStream is = null;
//                    try {
//                        is = new FileInputStream(mediaUrl);
//                        Bitmap bmpSource = BitmapFactory.decodeStream(is);
//                        File file = ImageUtil.saveFile(bmpSource, "/share.png");
//                        ToastUtils.show("图片已保存到" + file.getPath() + "下");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.i(TAG, "showMenu: " + e.toString());
//                        ToastUtils.show("保存图片错误");
//                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bp = ImageSaveUtils.returnBitMap(mediaUrl);
                            ImageSaveUtils.saveImageToPhotos(getApplicationContext(), bp);
                        }
                    }).start();
//                    if (TextUtils.isEmpty(entry.getMediaLocalPath()) && !TextUtils.isEmpty(mediaUrl)) {
//                        String imageFileName = DownloadManager.md5(mediaUrl) + mediaUrl.substring(mediaUrl.lastIndexOf('.'));
//                        File file = new File(Config.PHOTO_SAVE_DIR, imageFileName);
//                        if (file.exists()){
//                        }else {
//                            DownloadManager.download(entry.getMediaUrl(), Config.PHOTO_SAVE_DIR, imageFileName, new DownloadManager.SimpleOnDownloadListener() {
//                                @Override
//                                public void onUiSuccess(File file1) {
//                                    if (isFinishing()) {
//                                        return;
//                                    }
//                                    Toast.makeText(MMPreviewActivity.this, "图片保存成功", Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                        }
//
//                    }

                })


        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mm_preview);
        promptDialog = new PromptDialog(this);
        views = new SparseArray<>(3);
        viewPager = findViewById(R.id.viewPager);
        adapter = new MMPagerAdapter(entries);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(pageChangeListener);
        if (currentPosition == 0) {
            viewPager.post(() -> {
                pageChangeListener.onPageSelected(0);
            });
        } else {
            viewPager.setCurrentItem(currentPosition);
            pendingPreviewInitialMedia = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        entries = null;
    }

    public static void startActivity(Context context, List<MediaEntry> entries, int current, UiMessage msg) {
        if (entries == null || entries.isEmpty()) {
            Log.w(MMPreviewActivity.class.getSimpleName(), "message is null or empty");
            return;
        }
        MMPreviewActivity.entries = entries;
        MMPreviewActivity.currentPosition = current;
        MMPreviewActivity.uiMessage = msg;
        Intent intent = new Intent(context, MMPreviewActivity.class);
        context.startActivity(intent);
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

        UserInfo userInfo = ChatManager.Instance().getUserInfo(uid, true);
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
}
