package com.guozongkui.testimsdk.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.model.GroupResult;
import com.guozongkui.testimsdk.model.RegisterResult;
import com.guozongkui.testimsdk.model.Result;
import com.guozongkui.testimsdk.net.RetrofitHelper;
import com.guozongkui.testimsdk.net.RetrofitUtil;
import com.guozongkui.testimsdk.net.service.GroupService;
import com.guozongkui.testimsdk.net.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.rong.imkit.fragment.ConversationFragment.TAG;


public class MyConversationListFragment extends Fragment {

    ImageView btnMainMore;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public MyConversationListFragment() {
        // Required empty public constructor
    }

    public static MyConversationListFragment newInstance(String param1, String param2) {
        MyConversationListFragment fragment = new MyConversationListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnMainMore = view.findViewById(R.id.btn_main_more);
        btnMainMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewGroup();
            }
        });

        initConversationListView();
    }


    private void initConversationListView() {
        ConversationListFragment conversationListFragment=new ConversationListFragment();
        // 此处设置 Uri. 通过 appendQueryParameter 去设置所要支持的会话类型. 例如
        // .appendQueryParameter(Conversation.ConiversationType.PRIVATE.getName(),"false")
        // 表示支持单聊会话, false 表示不聚合显示, true 则为聚合显示
        Uri uri = Uri.parse("rong://" +
                this.getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlst")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();

        conversationListFragment.setUri(uri);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, conversationListFragment);
        transaction.commit();

        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {
            /**
             * 会话头像点击监听
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param targetId         被点击的用户id。
             * @return  true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            @Override
            public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String targetId) {
                return false;
            }
            /**
             * 会话头像长按监听
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param targetId         被点击的用户id。
             * @return true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String targetId) {
                return false;
            }

            /**
             * 会话列表中的 Item 长按监听
             *
             * @param context      上下文。
             * @param view         触发点击的 View。
             * @param conversation 长按时的会话条目
             * @return true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation conversation) {
                return false;
            }
            /**
             * 会话列表中的 Item 点击监听
             *
             * @param context      上下文。
             * @param view         触发点击的 View。
             * @param conversation 长按时的会话条目
             * @return true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            @Override
            public boolean onConversationClick(Context context, View view, UIConversation conversation) {
                return false;
            }
        });

        //消息搜索
//        FloatingActionButton btSendMsg = findViewById(R.id.floatingActionButton);
//        btSendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_conversation_list, container, false);
    }

    private void createNewGroup() {

        GroupService request = RetrofitHelper.getInstance().getRetrofit(getActivity()).create(GroupService.class);


        String[] memberList = {"NVNfLeGR7"," enWCs9zgb", "QSImlof2K"};

        final HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", "TestGroup123");
        paramsMap.put("memberIds", memberList);
        paramsMap.put("portraitUri", "https://s1.ax1x.com/2020/08/11/aqOXPP.jpg");

        RequestBody body = RetrofitUtil.createJsonRequest(paramsMap);

        Call<Result<GroupResult>> responseCall = request.createGroup(body);

        responseCall.enqueue(new Callback<Result<GroupResult>>() {
            @Override
            public void onResponse(Call<Result<GroupResult>> call, Response<Result<GroupResult>> response) {
                Log.d(TAG, "onResponse: code :" + response.body().getCode());
            }

            @Override
            public void onFailure(Call<Result<GroupResult>> call, Throwable t) {

            }
        });

    }

}