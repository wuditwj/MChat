package cn.wildfire.chat.app.personalcenter.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

public class UtilCountDownTimer extends CountDownTimer {

    /**
     * @param millisInFuture    表示以毫秒为单位 倒计时的总数
     *                          <p/>
     *                          例如 millisInFuture=1000 表示1秒
     * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
     *                          <p/>
     *                          例如: countDownInterval =1000 ;
     *                          表示每1000毫秒调用一次onTick()
     */
    private TextView textView;
    public UtilCountDownTimer(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setClickable(false);
        textView.setText("(" + millisUntilFinished / 1000 + ")重新获取");
    }

    @Override
    public void onFinish() {
        textView.setClickable(true);
        textView.setText("重新获取");
    }
}
