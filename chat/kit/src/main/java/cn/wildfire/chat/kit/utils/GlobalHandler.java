package cn.wildfire.chat.kit.utils;

import android.os.Handler;
import android.os.Message;

public class GlobalHandler extends Handler {

    private HandleMsgListener listener;
    private String Tag = GlobalHandler.class.getSimpleName();

    //使用单例模式创建GlobalHandler
    private GlobalHandler() {
    }

    private static class Holder {
        private static final GlobalHandler HANDLER = new GlobalHandler();
    }

    public static GlobalHandler getInstance() {
        return Holder.HANDLER;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getHandleMsgListener() != null) {
            getHandleMsgListener().handleMsg(msg);
        } else {
        }
    }

    public interface HandleMsgListener {
        void handleMsg(Message msg);
    }

    public void setHandleMsgListener(HandleMsgListener listener) {
        this.listener = listener;
    }

    public HandleMsgListener getHandleMsgListener() {
        return listener;
    }

}
