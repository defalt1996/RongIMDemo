package com.guozongkui.testimsdk;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class MyApplication extends Application {


    private static final String TAG = "MyApplication";
    private final String APP_KEY = "p5tvi9dspq3y4";
    private final String MY_TOKEN = "/qgp8L5eEZTZxEac/QvSAre4jgDJcfTnjLsVdLc1C6w=@3deq.cn.rongnav.com;3deq.cn.rongcfg.com";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onApplicationCreated...");
        RongIM.init(this, APP_KEY);

        RongIM.connect(MY_TOKEN, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess: Connected!");
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                Log.d(TAG, "onError: "+ connectionErrorCode);
            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                Log.d(TAG, "onDatabaseOpened: "+ databaseOpenStatus);
            }
        });




    }
}
