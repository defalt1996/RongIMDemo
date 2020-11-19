package com.guozongkui.testimsdk.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.model.MyUser;
import com.guozongkui.testimsdk.utils.DataGenerator;
import com.guozongkui.testimsdk.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imageloader.core.imageaware.ImageViewAware;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String mToken;
    private String mName;
    private String mAvatarUrl;
    private String mUserId;

    private TabLayout mTabLayout;
    private Fragment[] mFragments;

    Button mBtLogin;
    EditText mEtPhone;
    EditText mEtName;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();
        final MyUser user = intent.getParcelableExtra("user");
        if ( user != null){
            mToken = user.getToken();
            mName = user.getName();
            mAvatarUrl = user.getPortraitUri();
            mUserId = user.getUserId();
        }else {
            Log.d(TAG, "onCreate: get user = null !");
        }

        mFragments = DataGenerator.getFragments("TabLayout Tab");

        RongIM.connect(mToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d("Connect:", "onSuccess: Connected!");
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                Log.d("Connect:", "onError: "+ connectionErrorCode);
            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                Log.d("Connect:", "onDatabaseOpened: "+ databaseOpenStatus);
                Log.d(TAG, "connected! userinfo: userId = " + mUserId + ", name:" + mName + ", token: " + mToken);
            }
        });


//        mBtLogin = findViewById(R.id.bt_login);
//        mBtLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Map<String, Boolean> supportedConversation = new HashMap<>();
//                supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false);
//                RongIM.getInstance().startConversationList(MainActivity.this, supportedConversation);
//
////                if (mEtPhone.getText().toString().equals("")){
////                    Toast.makeText(MainActivity.this, "Pls input phone number", Toast.LENGTH_SHORT).show();
////                }else if (mEtName.getText().toString().equals("")){
////                    Toast.makeText(MainActivity.this, "Pls input your name", Toast.LENGTH_SHORT).show();
////                }else {
////                    Toast.makeText(MainActivity.this, "Registering... Pls wait", Toast.LENGTH_LONG).show();
////
////                    // 注册 需要App server来发送这个请求 客户端先不考虑
////
////
////                    // 跳转到会话列表
////
////                }
//
//
//            }
//        });

        //设置用户信息
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            @Override
            public UserInfo getUserInfo(String userId) {

                // 获取本地数据, 这里的逻辑应该是从AppServer获取，但是这里我从本地遍历json取数据了.
                String userData = Utils.getLocalJson(MainActivity.this, "qin_resource.json");
                Gson gson = new Gson();
                List<MyUser> myUserList = gson.fromJson(userData, new TypeToken<List<MyUser>>(){}.getType());
                for (MyUser item : myUserList) {
                    if (userId.equals(item.getUserId())) {
                        UserInfo userInfo = new UserInfo(userId, item.getName(), Uri.parse(item.getPortraitUri()));
                        Log.d(TAG, "callback : getUserInfo: userId =" + userId + "name =" + userInfo.getName());
                        return userInfo;
                    }
                }
                return null;
            }

        }, true);

        RongIMClient.getInstance().setKVStatusListener(new RongIMClient.KVStatusListener() {
            @Override
            public void onChatRoomKVSync(String s) {

            }

            @Override
            public void onChatRoomKVUpdate(String s, Map<String, String> map) {

            }

            @Override
            public void onChatRoomKVRemove(String s, Map<String, String> map) {

            }
        });

        initView();

        verifyStoragePermissions(this);


    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity ,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab 状态
                for(int i=0;i< mTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(DataGenerator.mTabResPressed[i]));
                    }else{
                        mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(DataGenerator.mTabRes[i]));
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.seal_ic_tab_chat)).setText(DataGenerator.mTabTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.seal_ic_tab_contacts)).setText(DataGenerator.mTabTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.seal_ic_tab_found)).setText(DataGenerator.mTabTitle[2]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.seal_ic_tab_me)).setText(DataGenerator.mTabTitle[3]));
    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = mFragments[0];
                break;
            case 1:
                fragment = mFragments[1];
                break;

            case 2:
                fragment = mFragments[2];
                break;
            case 3:
                fragment = mFragments[3];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }
}