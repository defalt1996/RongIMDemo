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

        //设置用户信息
        boolean isCacheUserInfo = true;
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            @Override
            public UserInfo getUserInfo(String userId) {
                UserInfo userInfo = new UserInfo(userId, "guoguoguo", Uri.parse("http://img.defalt.top/avatar.jpg"));
                Log.d(TAG, "getUserInfo: userId =" + userId + "name =" + userInfo.getName());
                return userInfo;
            }

        }, isCacheUserInfo);


    }
}
