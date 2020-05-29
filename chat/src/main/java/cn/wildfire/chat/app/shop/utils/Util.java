package cn.wildfire.chat.app.shop.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.wildfire.chat.app.shop.entity.PayInfo;
import cn.wildfirechat.chat.R;

public class Util {
    public static String decimalFormatMoney(double numbers) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        // 把转换后的货币String类型返回
        String numString = format.format(numbers);
        return numString;

    }

    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }
    public static String longToString(long date, String format) {
        long time = date*1000L;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }

    public static String ToDateYear(Date s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        res = simpleDateFormat.format(s);
        return res;
    }

    public static String hideCardNo(String cardNo) {
        if(TextUtils.isEmpty(cardNo)) {
            return cardNo;
        }

        int length = cardNo.length();
        int beforeLength = 0;
        int afterLength = 4;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(cardNo.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }

        return sb.toString();
    }
    public static String replace(String  str){
        StringBuilder sb=new StringBuilder(str);
        int length=str.length()/4+str.length();

        for(int i=0;i<length;i++){
            if(i%5==0){
                sb.insert(i," ");
            }
        }
        sb.deleteCharAt(0);
        return  sb.toString();
    }

    public static List<PayInfo> getPay(){
        List<PayInfo> list = new ArrayList<>();
        list.add(new PayInfo("支付宝支付","推荐已经安装支付宝客户端的用户使用", R.mipmap.ic_ali_pay,"alipay"));
//        list.add(new PayVo("微信支付","推荐已经安装微信客户端的用户使用",R.mipmap.ic_wechat_pay,"wx_h5"));

        return list;
    }
}
