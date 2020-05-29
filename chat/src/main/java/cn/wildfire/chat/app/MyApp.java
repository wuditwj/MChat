package cn.wildfire.chat.app;

import android.app.ActivityManager;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastQQStyle;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

import cn.wildfire.chat.app.message.FingerMessageContentViewHolder;
import cn.wildfire.chat.app.message.TypeCardMessageContentViewHolder;
import cn.wildfire.chat.app.message.redpackage.RedPackageMessageContentViewHolder;
import cn.wildfire.chat.app.third.location.viewholder.LocationMessageContentViewHolder;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.conversation.message.viewholder.MessageViewHolderManager;
import cn.wildfire.chat.kit.conversation.message.viewholder.RobRedMessageContentViewHolderSimple;
import cn.wildfirechat.chat.BuildConfig;
import cn.wildfirechat.message.Utils;
import cn.wildfirechat.push.PushService;
import okhttp3.internal.Util;


public class MyApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Config.validateConfig();
        Hawk.init(this).build();
        Utils.init(this);
//        ARouter.openLog();     // 打印日志
//        ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this);
        ToastUtils.init(this);
        ToastUtils.initStyle(new ToastQQStyle(this));
        // bugly，务必替换为你自己的!!!
        if ("wildfirechat.cn".equals(Config.IM_SERVER_HOST)) {
            CrashReport.initCrashReport(getApplicationContext(), Config.BUGLY_ID, false);
        }
        // 只在主进程初始化
        if (getCurProcessName(this).equals(BuildConfig.APPLICATION_ID)) {
            WfcUIKit wfcUIKit = WfcUIKit.getWfcUIKit();
            wfcUIKit.init(this);
            wfcUIKit.setAppServiceProvider(AppService.Instance());
            PushService.init(this, BuildConfig.APPLICATION_ID);
            MessageViewHolderManager.getInstance().registerMessageViewHolder(LocationMessageContentViewHolder.class);
            MessageViewHolderManager.getInstance().registerMessageViewHolder(RedPackageMessageContentViewHolder.class);
            MessageViewHolderManager.getInstance().registerMessageViewHolder(FingerMessageContentViewHolder.class);
            MessageViewHolderManager.getInstance().registerMessageViewHolder(TypeCardMessageContentViewHolder.class);
            MessageViewHolderManager.getInstance().registerMessageViewHolder(RobRedMessageContentViewHolderSimple.class);
            setupWFCDirs();
        }
    }

    private void setupWFCDirs() {
        File file = new File(Config.VIDEO_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Config.AUDIO_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Config.FILE_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Config.PHOTO_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
