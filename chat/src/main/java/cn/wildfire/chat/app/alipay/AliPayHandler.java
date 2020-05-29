package cn.wildfire.chat.app.alipay;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public abstract class AliPayHandler extends Handler {


	public void handleMessage(Message msg) {
			AliPayResult payResult = new AliPayResult((String) msg.obj);
			String resultInfo = payResult.getResult();
				Log.e("resultInfo",resultInfo);
			String resultStatus = payResult.getResultStatus();
			if (TextUtils.equals(resultStatus, "9000")) {
				onSuccess();
			} else {
				if (TextUtils.equals(resultStatus, "8000")) {
					onChecking();
				} else {
					onFail();
				}
			}
	}
	 protected abstract void onSuccess();
	 protected abstract void onChecking();
	 protected abstract void onFail();
}
