package cn.wildfire.chat.app;

import android.content.Context;
import android.os.Handler;

import androidx.multidex.MultiDexApplication;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import cn.wildfirechat.chat.R;


public class BaseApp extends MultiDexApplication {

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static long mMainThreadId;//主线程id
    private static Handler mHandler;//主线程Handler

    static{

        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多了";
        //设置全局的Header构建器
        ClassicsHeader.REFRESH_HEADER_REFRESHING="正在加载...";
        ClassicsHeader.REFRESH_HEADER_FINISH="加载完成";
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.white, R.color.black);//全局设置主题颜色
            layout.setEnableFooterFollowWhenLoadFinished(true);
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter footer =  new ClassicsFooter(context).setDrawableSize(15);
            footer.setTextSizeTitle(12);
            footer.setFinishDuration(0);
            return footer;
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //对全局属性赋值
        mContext = getApplicationContext();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        BaseApp.mContext = mContext;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }
}
