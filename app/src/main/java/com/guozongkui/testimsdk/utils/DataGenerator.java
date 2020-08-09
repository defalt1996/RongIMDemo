package com.guozongkui.testimsdk.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.ui.MyChatroomFragment;
import com.guozongkui.testimsdk.ui.MyContactFragment;
import com.guozongkui.testimsdk.ui.MyConversationListFragment;
import com.guozongkui.testimsdk.ui.MyMineFragment;

import io.rong.imkit.fragment.ConversationListFragment;

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.mipmap.seal_ic_tab_chat,R.mipmap.seal_ic_tab_contacts,R.mipmap.seal_ic_tab_found, R.mipmap.seal_ic_tab_me};
    public static final int []mTabResPressed = new int[]{R.mipmap.seal_ic_tab_chat_hover,R.mipmap.seal_ic_tab_contacts_hover,R.mipmap.seal_ic_tab_found_hover, R.mipmap.seal_ic_tab_me_hover};
    public static final String []mTabTitle = new String[]{"会话","通讯录","聊天室","我的"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[4];
        fragments[0] = MyConversationListFragment.newInstance(from,"");
        fragments[1] = MyContactFragment.newInstance(from,"");
        fragments[2] = MyChatroomFragment.newInstance(from,"");
        fragments[3] = MyMineFragment.newInstance(from,"");
        return fragments;
    }

//    /**
//     * 获取Tab 显示的内容
//     * @param context
//     * @param position
//     * @return
//     */
//    public static View getTabView(Context context, int position){
//        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
//        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
//        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
//        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
//        tabText.setText(mTabTitle[position]);
//        return view;
//    }

}
