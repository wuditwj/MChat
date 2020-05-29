package cn.wildfire.chat.app.alipay;

import android.app.Activity;
import android.os.Message;

import com.alipay.sdk.app.PayTask;


public class AliPayUtils {
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "";
    //商户私钥，pkcs8格式
    private static final String RSA_PRIVATE = "";
    //支付宝公钥
    private static final String RSA_PUBLIC = "";

	private AliPayHandler mAliPayHandler ;
	private PayTask mPayTask ;

	/**
	* 构造函数
	* @param mActivity
	* @param handler
	*/
	public AliPayUtils(Activity mActivity, AliPayHandler handler){
		mAliPayHandler=handler;
		// 构造PayTask 对象
		mPayTask = new PayTask(mActivity);
	}


	/**
	* 支付
	* @param orderId
	* @param subject
	* @param body
	* @param price   订单总价 单位为元 可保留小数 如 12.34
	*/
	public void pay(final String orderInfo)  {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 调用支付接口，获取支付结果
				String result = mPayTask.pay(orderInfo,true);
				Message msg = new Message();
				msg.obj = result;
				mAliPayHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}




	/**
	 * 获取SDK版本号
	 */
	public String getSDKVersion() {
        return mPayTask.getVersion();
	}


}
