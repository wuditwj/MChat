package cn.wildfire.chat.app;

import java.util.List;


public interface UserSettingCallback {
    void onSuccess();

    void onFail(int errorCode);
}
