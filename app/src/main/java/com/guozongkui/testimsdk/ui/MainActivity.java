package com.guozongkui.testimsdk.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.utils.DataGenerator;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String MY_TOKEN = "O8nJqMyZc9DZxEac/QvSAh3l79AWIO/jGmsOVRo/cbY=@3deq.cn.rongnav.com;3deq.cn.rongcfg.com";

    private TabLayout mTabLayout;
    private Fragment[] mFragments;

    Button mBtLogin;
    EditText mEtPhone;
    EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragments = DataGenerator.getFragments("TabLayout Tab");


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
        boolean isCacheUserInfo = true;
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            @Override
            public UserInfo getUserInfo(String userId) {
                UserInfo userInfo = new UserInfo(userId, "guoguoguo", Uri.parse("https://s1.ax1x.com/2020/08/05/a6swT0.jpg"));
                Log.d(TAG, "getUserInfo: userId =" + userId + "name =" + userInfo.getName());
                return userInfo;
            }

        }, isCacheUserInfo);

        initView();


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