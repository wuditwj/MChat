package cn.wildfire.chat.app.http;


import java.io.File;
import java.util.List;

public interface HttpService {
    void onSendRedPackage(String formUser, String toUser, String redMoney, String redNumber, String redTitle, String sendType, String pwd, HttpHandler httpHandler);

    void CheckPayPwd(HttpHandler httpHandler);

    void onLogin(String mobile, String pwd, HttpHandler httpHandler);

    void getGoods(int page, int pageSize, HttpHandler httpHandler);

    void getGoodInfo(String goodsId, HttpHandler httpHandler);

    void getOrderList(String userId, int page, int pageSize, HttpHandler httpHandler);

    void submit(String goodsOrder, HttpHandler httpHandler);

    void checkRedPackageStatus(String sendLogId, HttpHandler httpHandler);

    void getRedPackage(String robUser, String sendLogId, String sendType, HttpHandler httpHandler);//领取红包

    void getAddress(String userId, HttpHandler httpHandler);

    void delAddress(String addrId, HttpHandler httpHandler);

    void modifyAddress(String addressId, String name, String mobile, String areaName, String addr, HttpHandler httpHandler);

    void addAddress(String userId, String name, String mobile, String areaName, String addr, HttpHandler httpHandler);

    void getRedPackageDetail(String sendLogId, HttpHandler httpHandler);

    void getOrderDetail(String orderId, HttpHandler httpHandler);

    void onPay(String orderId, String type, String userId, HttpHandler httpHandler);

    void isPayPwd(String userId, HttpHandler httpHandler);

    void getCode(String mobile, HttpHandler httpHandler);

    void next(String mobile, String code, HttpHandler httpHandler);

    void setAccount(String userId, String pwd, String oldPwd, HttpHandler httpHandler);

    void getAmount(String userId, HttpHandler httpHandler);

    void onRecharge(String money, String userid, HttpHandler httpHandler);

    void onPayOnWallet(String orderId, String type, String userId, HttpHandler httpHandler);

    void withDraw(String userid, String money, String payCard, String checkMoney, HttpHandler httpHandler);

    void getUserBankList(String userid, HttpHandler httpHandler);

    void checkBankType(String bankAccount, HttpHandler httpHandler);

    void addUserBank(String userid, String accountOpening, String bank, String bankAccount, String branch, String realname, String idcard, String mobile, String bankLogo, HttpHandler httpHandler);

    void delUserBank(String id, HttpHandler httpHandler);

    void deleteUserBill(String userid, HttpHandler httpHandler);

    void billList(String userid, String type, String checkTime, int pageSize, int page, HttpHandler httpHandler);

    void myReceivedRed(String userId, int page, int pageSize, HttpHandler httpHandler);

    void mySendRed(String userId, int page, int pageSize, HttpHandler httpHandler);

    void setType(String userId, String type, String urlPath, HttpHandler httpHandler);

    void serachStatus(String userId, HttpHandler httpHandler);

    void updatePwd(String userId, String pwd, String oldPwd, HttpHandler httpHandler);

    void requestAuthCode(String mobile, HttpHandler httpHandler);

    void onLoginSMS(String  mobile,String code,HttpHandler httpHandler);

    void onWeChatLogin(String openId,HttpHandler httpHandler);

    void onBindWeChat(String openId,String disName,String pic,String mobile,String code,HttpHandler httpHandler);

    void onRegister(String mobile,String pwd,String code,HttpHandler httpHandler);

    void addOpinion(String userId, String info, HttpHandler httpHandler);

    void onSubmit(String info, List<File> fileImg,HttpHandler httpHandler);

    void sysMessageList(int page, int pageSize,HttpHandler httpHandler);
}
