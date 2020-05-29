package cn.wildfire.chat.kit.conversation.ext.core;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import cn.wildfire.chat.kit.conversation.ext.ExampleAudioInputExt;
import cn.wildfire.chat.kit.conversation.ext.FileExt;
import cn.wildfire.chat.kit.conversation.ext.FingerGameExt;
import cn.wildfire.chat.kit.conversation.ext.ImageExt;
import cn.wildfire.chat.kit.conversation.ext.LocationExt;
import cn.wildfire.chat.kit.conversation.ext.RedPackageExt;
import cn.wildfire.chat.kit.conversation.ext.RedPackageExt1;
import cn.wildfire.chat.kit.conversation.ext.ShakeExt;
import cn.wildfire.chat.kit.conversation.ext.ShootExt;
import cn.wildfire.chat.kit.conversation.ext.VoipExt;
import cn.wildfirechat.model.Conversation;

public class ConversationExtManager {
    private static ConversationExtManager instance;
    private List<ConversationExt> conversationExts;

    private ConversationExtManager(Conversation conversation) {
        conversationExts = new ArrayList<>();
        init(conversation);
    }

    public static synchronized ConversationExtManager getInstance(Conversation conversation) {
        instance = new ConversationExtManager(conversation);
        return instance;
    }

    private void init(Conversation conversation) {
        registerExt(ImageExt.class);//相册
        if (conversation.type == Conversation.ConversationType.Group) {
            registerExt(RedPackageExt.class);// 普通红包
            registerExt(RedPackageExt1.class);//专属红包
            registerExt(LocationExt.class);//位置
        } else if (conversation.type == Conversation.ConversationType.Single) {
            registerExt(RedPackageExt.class);// 普通红包
            registerExt(LocationExt.class);//位置
            registerExt(ShakeExt.class);//戳一戳
        }


        registerExt(FingerGameExt.class);//猜拳
    }

    public void registerExt(Class<? extends ConversationExt> clazz) {
        Constructor constructor;
        try {
            constructor = clazz.getConstructor();
            ConversationExt ext = (ConversationExt) constructor.newInstance();
            conversationExts.add(ext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterExt(Class<? extends ConversationExt> clazz) {
        // TODO
        Constructor constructor;
        try {
            constructor = clazz.getConstructor();
            ConversationExt ext = (ConversationExt) constructor.newInstance();
            conversationExts.remove(ext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ConversationExt> getConversationExts(Conversation conversation) {
        List<ConversationExt> currentExts = new ArrayList<>();
        for (ConversationExt ext : this.conversationExts) {
            if (!ext.filter(conversation)) {
                currentExts.add(ext);
            }
        }
        return currentExts;
    }
}
