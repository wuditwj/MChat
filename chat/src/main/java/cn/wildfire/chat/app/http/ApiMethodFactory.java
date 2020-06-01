package cn.wildfire.chat.app.http;


import android.text.TextUtils;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.util.List;

import cn.wildfire.chat.app.login.LoginActivity;
import cn.wildfire.chat.app.login.SMSLoginActivity;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfirechat.remote.ChatManager;

/**
 * 接口工厂类
 */
public class ApiMethodFactory implements HttpService {
    private static ApiMethodFactory instance = null;

    private ApiMethodFactory() {
    }

    public static synchronized ApiMethodFactory getInstance() {
        if (instance == null) {
            synchronized (ApiMethodFactory.class) {
                if (instance == null) {
                    instance = new ApiMethodFactory();
                }
            }
        }
        return instance;
    }


    @Override
    public void onLogin(String mobile, String pwd, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", mobile);
        httpParams.put("pwd", pwd);
        httpParams.put("platform", new Integer(2));
        try {
            httpParams.put("clientId", ChatManagerHolder.gChatManager.getClientId());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("网络出来问题了。。。");
            return;
        }
        HttpUtil.getInstance().post("userManage/loginPwd", httpParams, httpHandler);
    }

    @Override
    public void onSendRedPackage(String formUser, String toUser, String redMoney, String redNumber, String redTitle, String sendType, String pwd, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("formUser", formUser);
        httpParams.put("redMoney", redMoney);
        httpParams.put("redNumber", redNumber);
        httpParams.put("redTitle", redTitle);
        httpParams.put("toUser", toUser);
        httpParams.put("sendType", sendType);//1单发 2群发 3//定向红包
        httpParams.put("pwd", pwd);//1单发 2群发
        HttpUtil.getInstance().post("redManage/sendRed", httpParams, httpHandler);
    }

    @Override
    public void CheckPayPwd(HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", ChatManager.Instance().getUserId());
        HttpUtil.getInstance().post("userManage/checkWallet", httpParams, httpHandler);

    }

    @Override
    public void getGoods(int page, int pageSize, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("page", page);
        httpParams.put("pageSize", 10);
        HttpUtil.getInstance().post("goodsManage/getGoodsInfoList", httpParams, httpHandler);
    }

    @Override
    public void getGoodInfo(String goodsId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("goodsId", goodsId);
        HttpUtil.getInstance().post("goodsManage/getGoodsInfo", httpParams, httpHandler);
    }

    @Override
    public void getOrderList(String userId, int page, int pageSize, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("page", page);
        httpParams.put("pageSize", pageSize);
        HttpUtil.getInstance().post("goodsManage/getGoodsOrderList", httpParams, httpHandler);
    }

    @Override
    public void submit(String goodsOrder, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("goodsOrder", goodsOrder);
        HttpUtil.getInstance().post("goodsManage/addGoodsOrder", httpParams, httpHandler);
    }

    @Override
    public void checkRedPackageStatus(String sendLogId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("sendLogId", sendLogId);
        HttpUtil.getInstance().post("redManage/checkRedStatus", httpParams, httpHandler);
    }

    @Override
    public void getRedPackage(String robUser, String sendLogId, String sendType, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("robUser", robUser);
        httpParams.put("sendLogId", sendLogId);
        httpParams.put("sendType", sendType);
        HttpUtil.getInstance().post("redManage/robRed", httpParams, httpHandler);
    }

    @Override
    public void getAddress(String userId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        HttpUtil.getInstance().post("goodsManage/getUserAdderList", httpParams, httpHandler);
    }

    @Override
    public void delAddress(String addrId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("addrId", addrId);
        HttpUtil.getInstance().post("goodsManage/delUserAdder", httpParams, httpHandler);
    }

    @Override
    public void modifyAddress(String addressId, String name, String mobile, String areaName, String addr, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("addrId", addressId);
        httpParams.put("name", name);
        httpParams.put("mobile", mobile);
        httpParams.put("areaName", areaName);
        httpParams.put("addr", addr);
        HttpUtil.getInstance().post("goodsManage/updateUserArrer", httpParams, httpHandler);
    }

    @Override
    public void addAddress(String userId, String name, String mobile, String areaName, String addr, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("name", name);
        httpParams.put("mobile", mobile);
        httpParams.put("areaName", areaName);
        httpParams.put("addr", addr);
        HttpUtil.getInstance().post("goodsManage/addUserAdder", httpParams, httpHandler);
    }

    @Override
    public void getRedPackageDetail(String sendLogId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("sendLogId", sendLogId);
        httpParams.put("robUser", ChatManagerHolder.gChatManager.getUserId());
        HttpUtil.getInstance().post("redManage/redListInfo", httpParams, httpHandler);
    }

    @Override
    public void getOrderDetail(String orderId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("orderId", orderId);
        HttpUtil.getInstance().post("goodsManage/getGoodsOrderInfo", httpParams, httpHandler);
    }

    @Override
    public void onPay(String orderId, String type, String userId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("orderId", orderId);
        httpParams.put("type", type);
        httpParams.put("userId", userId);
        HttpUtil.getInstance().post("goodsManage/payType", httpParams, httpHandler);
    }

    @Override
    public void isPayPwd(String userId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        HttpUtil.getInstance().post("userManage/checkWallet", httpParams, httpHandler);
    }

    @Override
    public void getCode(String mobile, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", mobile);
        HttpUtil.getInstance().post("userManage/getYzm", httpParams, httpHandler);
    }

    @Override
    public void next(String mobile, String code, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", mobile);
        httpParams.put("code", code);
        HttpUtil.getInstance().post("userManage/checkYzm", httpParams, httpHandler);
    }

    @Override
    public void setAccount(String userId, String pwd, String oldPwd, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("pwd", pwd);
        httpParams.put("oldPwd", oldPwd);
        HttpUtil.getInstance().post("userManage/userWallet", httpParams, httpHandler);
    }

    @Override
    public void getAmount(String userId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        HttpUtil.getInstance().post("userManage/getAccountMoney", httpParams, httpHandler);
    }

    @Override
    public void onRecharge(String money, String userid, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("money", money);
        httpParams.put("userid", userid);
        HttpUtil.getInstance().post("billManage/userRecharge", httpParams, httpHandler);
    }

    @Override
    public void onPayOnWallet(String orderId, String type, String userId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("orderId", orderId);
        httpParams.put("type", type);
        httpParams.put("userId", userId);
        HttpUtil.getInstance().post("payManage/payType", httpParams, httpHandler);
    }

    @Override
    public void withDraw(String userid, String money, String payCard, String checkMoney, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userid", userid);
        httpParams.put("money", money);
        httpParams.put("payCard", payCard);
        httpParams.put("checkMoney", checkMoney);
        HttpUtil.getInstance().post("billManage/userWithdrawal", httpParams, httpHandler);
    }

    @Override
    public void getUserBankList(String userid, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userid", userid);
        HttpUtil.getInstance().post("bankManage/getUserBankList", httpParams, httpHandler);
    }

    @Override
    public void checkBankType(String bankAccount, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("bankAccount", bankAccount);
        HttpUtil.getInstance().post("bankManage/checkBankType", httpParams, httpHandler);
    }

    @Override
    public void addUserBank(String userid, String accountOpening, String bank, String bankAccount, String branch, String realname, String idcard, String mobile, String bankLogo, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userid", userid);
        httpParams.put("accountOpening", accountOpening);
        httpParams.put("bank", bank);
        httpParams.put("bankAccount", bankAccount);
        httpParams.put("branch", branch);
        httpParams.put("realname", realname);
        httpParams.put("idcard", idcard);
        httpParams.put("mobile", mobile);
        httpParams.put("bankLogo", bankLogo);
        HttpUtil.getInstance().post("bankManage/addUserBank", httpParams, httpHandler);
    }

    @Override
    public void delUserBank(String id, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("id", id);
        HttpUtil.getInstance().post("bankManage/delUserBank", httpParams, httpHandler);
    }

    @Override
    public void deleteUserBill(String userid, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userid", userid);
        HttpUtil.getInstance().post("billManage/deleteUserBill", httpParams, httpHandler);
    }

    @Override
    public void billList(String userid, String type, String checkTime, int pageSize, int page, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userid", userid);
        httpParams.put("type", type);
        httpParams.put("checkTime", checkTime);
        httpParams.put("pageSize", pageSize);
        httpParams.put("page", page);
        HttpUtil.getInstance().post("billManage/billList", httpParams, httpHandler);
    }

    @Override
    public void myReceivedRed(String userId, int page, int pageSize, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("page", page);
        httpParams.put("pageSize", pageSize);
        HttpUtil.getInstance().post("redManage/myReceivedRed", httpParams, httpHandler);
    }

    @Override
    public void mySendRed(String userId, int page, int pageSize, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("page", page);
        httpParams.put("pageSize", pageSize);
        HttpUtil.getInstance().post("redManage/mySendRed", httpParams, httpHandler);
    }

    @Override
    public void setType(String userId, String type, String urlPath, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("type", type);
        HttpUtil.getInstance().post(urlPath, httpParams, httpHandler);
    }

    @Override
    public void serachStatus(String userId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        HttpUtil.getInstance().post("userManage/serachStatus", httpParams, httpHandler);
    }

    @Override
    public void updatePwd(String userId, String pwd, String oldPwd, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("pwd", pwd);
        httpParams.put("oldPwd", oldPwd);
        HttpUtil.getInstance().post("userManage/updatePwd", httpParams, httpHandler);
    }

    @Override
    public void requestAuthCode(String mobile, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", mobile);
        httpParams.put("platform", new Integer(2));
        HttpUtil.getInstance().post("userManage/getYzm", httpParams, httpHandler);
    }

    @Override
    public void onLoginSMS(String mobile, String code, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", mobile);
        httpParams.put("code", code);
        try {
            httpParams.put("clientId", ChatManagerHolder.gChatManager.getClientId());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("网络出来问题了...");
            return;
        }

        HttpUtil.getInstance().post("login", httpParams, httpHandler);
    }

    @Override
    public void onWeChatLogin(String openId, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("openId", openId);
        httpParams.put("platform", new Integer(2));
        try {
            httpParams.put("clientId", ChatManagerHolder.gChatManager.getClientId());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("网络出来问题了...");
            return;
        }
        HttpUtil.getInstance().post("userManage/weiLogin", httpParams, httpHandler);

    }

    @Override
    public void onBindWeChat(String openId, String disName, String pic, String mobile, String code, HttpHandler httpHandler) {
        HttpParams httpParams1 = new HttpParams();
        httpParams1.put("openId", openId);
        httpParams1.put("disName", disName);
        httpParams1.put("pic", pic);
        httpParams1.put("mobile", mobile);
        httpParams1.put("code", code);
        httpParams1.put("platform", new Integer(2));
        try {
            httpParams1.put("clientId", ChatManagerHolder.gChatManager.getClientId());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("网络出来问题了。。。");
            return;
        }
        HttpUtil.getInstance().post("userManage/weiBind", httpParams1, httpHandler);


    }

    @Override
    public void onRegister(String mobile, String pwd, String code, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", mobile);
        httpParams.put("pwd", pwd);
        httpParams.put("code", code);
        httpParams.put("platform", new Integer(2));
        try {
            httpParams.put("clientId", ChatManagerHolder.gChatManager.getClientId());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("网络出来问题了。。。");
            return;
        }
        HttpUtil.getInstance().post("userManage/userRegistere", httpParams, httpHandler);


    }

    @Override
    public void addOpinion(String userId, String info, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", userId);
        httpParams.put("info", info);
        HttpUtil.getInstance().post("userManage/addOpinion", httpParams, httpHandler);
    }

    @Override
    public void onSubmit(String info, List<File> fileImg, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId", ChatManagerHolder.gChatManager.getUserId());
        httpParams.put("info", info);
        httpParams.putFileParams("fileImg", fileImg);
        HttpUtil.getInstance().post("userManage/addComplaint", httpParams, httpHandler);
    }

    @Override
    public void sysMessageList(int page, int pageSize, HttpHandler httpHandler) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("page", page);
        httpParams.put("pageSize", pageSize);
        HttpUtil.getInstance().post("billManage/sysMessageList", httpParams, httpHandler);
    }
}
