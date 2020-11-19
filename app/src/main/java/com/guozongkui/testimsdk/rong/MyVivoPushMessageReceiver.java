package com.guozongkui.testimsdk.rong;

import android.content.Context;

import com.vivo.push.model.UPSNotificationMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.rong.common.RLog;
import io.rong.push.PushManager;
import io.rong.push.PushType;
import io.rong.push.RongPushClient;
import io.rong.push.notification.PushNotificationMessage;
import io.rong.push.platform.vivo.VivoPushMessageReceiver;

public class MyVivoPushMessageReceiver extends VivoPushMessageReceiver {

    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage message) {

        RLog.v("VivoPushMessageReceiver",
                "onNotificationMessageClicked is called. " + (message.getParams() != null ? message.getParams().toString() : ""));
        PushNotificationMessage pushNotificationMessage =  transformVivoToPushMessage(message.getTitle(), message.getContent(), message.getParams());
        if (pushNotificationMessage != null) {
            PushManager.getInstance().onNotificationMessageClicked(context, PushType.VIVO, pushNotificationMessage);
        }

    }

    public static PushNotificationMessage transformVivoToPushMessage(String title, String content, Map<String, String> params) {
        if (params == null){
            return null;
        }

        PushNotificationMessage pushNotificationMessage = null;
        String rc = params.get("rc");
        if (rc != null) {
            try {
                JSONObject rcJson = new JSONObject(rc);
                pushNotificationMessage = new PushNotificationMessage();


                pushNotificationMessage.setPushTitle(title);
                pushNotificationMessage.setPushContent(content);


                int conversationType = rcJson.optInt("conversationType");
                pushNotificationMessage.setConversationType(RongPushClient.ConversationType.setValue(conversationType));


                int sourceType = rcJson.optInt("sourceType");
                pushNotificationMessage.setSourceType(getType(sourceType));


                pushNotificationMessage.setSenderId(rcJson.optString("fromUserId"));
                pushNotificationMessage.setObjectName(rcJson.optString("objectName"));
                pushNotificationMessage.setPushId(rcJson.optString("id"));
                pushNotificationMessage.setToId(rcJson.optString("tId"));
                pushNotificationMessage.setTargetId(rcJson.optString("targetId"));


                String appData = params.get("appData");
                if (appData != null) {
                    pushNotificationMessage.setPushData(appData);
                }
            } catch (JSONException e) {
                RLog.e("PushUtils", "transformToPushMessage:" + e.getMessage());
                pushNotificationMessage = null;
            }
        }
        return pushNotificationMessage;
    }

    public static PushNotificationMessage.PushSourceType getType(int type) {
        for (PushNotificationMessage.PushSourceType sourceType : PushNotificationMessage.PushSourceType.values()) {
            if (sourceType.ordinal() == type) {
                return sourceType;
            }
        }


        return PushNotificationMessage.PushSourceType.LOCAL_MESSAGE;
    }
}
