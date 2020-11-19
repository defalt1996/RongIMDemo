package com.guozongkui.testimsdk.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.model.GroupResult;
import com.guozongkui.testimsdk.model.Result;
import com.guozongkui.testimsdk.net.RetrofitHelper;
import com.guozongkui.testimsdk.net.RetrofitUtil;
import com.guozongkui.testimsdk.net.service.GroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
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
//                createNewGroup();
//                sendImageMessage();
//                insertOutgoingMessage();
            sendLocationMessage();
            }
        });

        initConversationListView();
    }

    private Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        Log.d(TAG, "getUriFromDrawableRes: path = " + path);
        return Uri.parse(path);
    }


    private void sendLocationMessage(){
        double lat = 40.0317727;
        double lng = 116.4175057;
        String poi = "北辰·泰岳";
        String path = "http://restapi.amap.com/v3/staticmap?location=119.9925486,30.2768368&zoom=16&scale=2&size=408*240&markers=mid,,A:119.9925486,30.2768368&key=e09af6a2b26c02086e9216bd07c960ae";

        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        String targetId = "1001";

        LocationMessage locationMessage = LocationMessage.obtain(lat, lng, poi, Uri.parse(path));
        Message message = Message.obtain(targetId, conversationType, locationMessage);
        RongIM.getInstance().sendLocationMessage(message, null ,null,new IRongCallback.ISendMessageCallback() {
            /**
             * 消息发送前回调, 回调时消息已存储数据库
             * @param message 已存库的消息体
             */
            @Override
            public void onAttached(Message message) {

            }
            /**
             * 消息发送成功。
             * @param message 发送成功后的消息体
             */
            @Override
            public void onSuccess(Message message) {

            }

            /**
             * 消息发送失败
             * @param message   发送失败的消息体
             * @param errorCode 具体的错误
             */
            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.d(TAG, "onError: " + errorCode);
            }
        });


    }

    private void insertOutgoingMessage(){

        String content = "insert!insert!";
        TextMessage messageContent = TextMessage.obtain(content);

        long sendTime = System.currentTimeMillis();

//        messageContent.setDestruct(true);
//        messageContent.setDestructTime(sendTime);


        Log.d(TAG, "insertOutgoingMessage: current time stamp = "+ System.currentTimeMillis());
        Toast.makeText(getActivity(), "current time stamp = "+ System.currentTimeMillis(), Toast.LENGTH_LONG).show();

        RongIM.getInstance().insertIncomingMessage(Conversation.ConversationType.PRIVATE, "NVNfLeGR7", "enWCs9zgb", new Message.ReceivedStatus(0x1), messageContent, sendTime, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                Log.d(TAG, "onSuccess: insert message successful! ");

                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        for (Conversation item : conversations){
                            String latestMessage = item.getLatestMessage().toString();
                            Log.d(TAG, "onSuccess: latest message = "+ latestMessage);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void sendImageMessage() {

//        Uri imageUriFromDrawable = getUriFromDrawableRes(getActivity(), R.drawable.ic_launcher_round);

//        Uri localUri = Uri.parse("file:///storage/emulated/0/DCIM/Camera/u=3591494791,2273718724&fm=193&app=53&n=0&g=0n&f=jpeg.jpg");
        Uri localUri = Uri.parse("file:///storage/emulated/0/DCIM/Camera/IMG_20201101_161228.jpg");

        ImageMessage imageMessage = ImageMessage.obtain(localUri,localUri);
        

        RongIM.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, "1001", imageMessage,null, null, new RongIMClient.SendImageMessageCallback(){

            @Override
            public void onAttached(Message message) {
                Log.d(TAG, "onAttached: ");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onSuccess(Message message) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onProgress(Message message, int i) {

            }
        });



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
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "true")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "true")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();

        conversationListFragment.setUri(uri);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, conversationListFragment);
        transaction.commit();


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
                String id = response.body().getResult().id;

            }

            @Override
            public void onFailure(Call<Result<GroupResult>> call, Throwable t) {

            }
        });


    }





}