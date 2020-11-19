package com.guozongkui.testimsdk;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.guozongkui.testimsdk.im.message.GroupApplyMessage;
import com.guozongkui.testimsdk.im.message.SealContactNotificationMessage;
import com.guozongkui.testimsdk.im.message.SealGroupConNtfMessage;
import com.guozongkui.testimsdk.im.message.SealGroupNotificationMessage;
import com.guozongkui.testimsdk.im.provider.GroupApplyMessageProvider;
import com.guozongkui.testimsdk.im.provider.SealGroupConNtfMessageProvider;
import com.guozongkui.testimsdk.rong.MyExtensionModule;
import com.guozongkui.testimsdk.rong.QQEmoji;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.rong.common.utils.SSLUtils;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;
import io.rong.push.pushconfig.PushConfig;

public class MyApplication extends Application {


    private static final String TAG = "MyApplication";
    private final String APP_KEY = "p5tvi9dspq3y4";


//    private final String APP_KEY = "n19jmcy59f1q9";

//    private final String MY_TOKEN = "/qgp8L5eEZTZxEac/QvSAre4jgDJcfTnjLsVdLc1C6w=@3deq.cn.rongnav.com;3deq.cn.rongcfg.com";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onApplicationCreated...");

        SSLUtils.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        RongIM.init(this, APP_KEY, false);
//        RongIM.registerMessageTemplate(new MyTextMessageItemProvider());

        PushConfig config = new PushConfig.Builder()
                .enableVivoPush(true)
                .build();
        RongPushClient.setPushConfig(config);


        RongIM.registerMessageType(SealGroupNotificationMessage.class);
        RongIM.registerMessageType(SealContactNotificationMessage.class);
//        RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
//        RongIM.registerMessageTemplate(new SealGroupNotificationMessageItemProvider());
        RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
        RongIM.registerMessageType(SealGroupConNtfMessage.class);
        RongIM.registerMessageTemplate(new SealGroupConNtfMessageProvider());
        RongIM.registerMessageType(GroupApplyMessage.class);
        RongIM.getInstance().registerConversationTemplate(new GroupApplyMessageProvider());

        QQEmoji.init(this);

        registerExtensionPlugin();


        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {

                Log.d(TAG, "onReceived: " + message.getObjectName());

                RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Toast.makeText(MyApplication.this, "Unread count :" + integer, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });

                return false;
//
//
//                if (message.getContent() instanceof TextMessage){
//                    TextMessage textMessage = (TextMessage) message.getContent();
//                    textMessage.getContent();
//                    textMessage.getExtra();
//                }
//
//                Log.d(TAG, "onReceived: message content" + message.getContent());
//                return false;
            }
        });
        RongIM.getInstance().getCurrentConnectionStatus();

        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String s) {

                return null;
            }
        }, true);

        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {

//                return null;  返回null就被拦截了

                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        });

        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                Toast.makeText(context, "Do onUserPortraitClick", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                Toast.makeText(context, "Do onUserPortraitLongClick", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                Toast.makeText(context, "Do onMessageClick", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s, Message message) {
                Toast.makeText(context, "Do onMessageLinkClick", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                Toast.makeText(context, "Do onMessageLongClick", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        String jsonStr = "{\"rc\":\"{\\\"conversationType\\\":\\\"1\\\",\\\"targetId\\\":\\\"userid8\\\",\\\"sourceType\\\":\\\"0\\\",\\\"fromUserId\\\":\\\"userid8\\\",\\\"objectName\\\":\\\"RC:TxtMsg\\\",\\\"id\\\":\\\"BLCG-G8TC-U7E6-KV7P\\\",\\\"tId\\\":\\\"doctorid3\\\"}\"}";
        String fixStr1 = jsonStr.replace("\\", "");
        String fixStr2 = fixStr1.replace("\"rc\":\"", "\"rc\":" );
        String result = fixStr2.replace("\"}\"", "\"}");

        Log.d(TAG, "result jsonStr: "+ result);

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(result);
            String options = jsonObject.getString("rc");
            JsonObject object = (JsonObject) new JsonParser().parse(options);

            String targetId = object.getAsJsonObject().get("targetId").getAsString();

            Log.d(TAG, "analyse json targetId: " + targetId);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {


            }
        });

//        RongIM.getInstance().enableNewComingMessageIcon(true);




    }



    private void registerExtensionPlugin() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }



    }




}
