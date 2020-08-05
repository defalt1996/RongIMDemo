package com.guozongkui.testimsdk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class ConversationListActivity extends AppCompatActivity {

    private static final String TAG = "ConversationListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        ConversationListFragment conversationListFragment=new ConversationListFragment();
        // 此处设置 Uri. 通过 appendQueryParameter 去设置所要支持的会话类型. 例如
        // .appendQueryParameter(Conversation.ConiversationType.PRIVATE.getName(),"false")
        // 表示支持单聊会话, false 表示不聚合显示, true 则为聚合显示
        Uri uri = Uri.parse("rong://" +
                this.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlst")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();

        conversationListFragment.setUri(uri);
        FragmentManager manager = getSupportFragmentManager();
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

        //测试 发送一条文本消息
        FloatingActionButton btSendMsg = findViewById(R.id.floatingActionButton);
        btSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = "test123123123";

                Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
                String targetId = "23456";

                TextMessage messageContent = TextMessage.obtain(content);
                Message message = Message.obtain(targetId, conversationType, messageContent);
                RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
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

                    }
                });
            }
        });


    }

}