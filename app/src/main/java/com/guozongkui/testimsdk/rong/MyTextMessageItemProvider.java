package com.guozongkui.testimsdk.rong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozongkui.testimsdk.R;

import java.lang.ref.WeakReference;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongKitIntent;
import io.rong.imkit.destruct.DestructManager;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AutoLinkTextView;
import io.rong.imkit.widget.ILinkClickListener;
import io.rong.imkit.widget.LinkTextViewMovementMethod;
import io.rong.imkit.widget.provider.TextMessageItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

@ProviderTag(
        messageContent = TextMessage.class,
        showReadState = true
)
public class MyTextMessageItemProvider extends TextMessageItemProvider {
    private static final String TAG = "TextMessageItemProvider";

    public MyTextMessageItemProvider(){
        super();
    }

    private static class ViewHolder {
        AutoLinkTextView message;
        TextView unRead;
        FrameLayout sendFire;
        FrameLayout receiverFire;
        ImageView receiverFireImg;
        TextView receiverFireText;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_destruct_text_message, null);
        MyTextMessageItemProvider.ViewHolder holder = new MyTextMessageItemProvider.ViewHolder();
        holder.message = view.findViewById(android.R.id.text1);
        holder.unRead = view.findViewById(R.id.tv_unread);
        holder.sendFire = view.findViewById(R.id.fl_send_fire);
        holder.receiverFire = view.findViewById(R.id.fl_receiver_fire);
        holder.receiverFireImg = view.findViewById(R.id.iv_receiver_fire);
        holder.receiverFireText = view.findViewById(R.id.tv_receiver_fire);
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(TextMessage data) {
        return null;
    }

    @Override
    public Spannable getContentSummary(Context context, TextMessage data) {
        if (data == null)
            return null;
        if (data.isDestruct()) {
            return new SpannableString(context.getString(R.string.rc_message_content_burn));
        }
        String content = data.getContent();
        if (content != null) {
            if (content.length() > 100) {
                content = content.substring(0, 100);
            }
            return new SpannableString(AndroidEmoji.ensure(content));
        }
        return null;
    }

    @Override
    public void onItemClick(View view, int position, TextMessage content, UIMessage message) {
        MyTextMessageItemProvider.ViewHolder holder = (MyTextMessageItemProvider.ViewHolder) view.getTag();
        if (content != null && content.isDestruct() && !(message.getMessage().getReadTime() > 0)) {
            holder.unRead.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
            holder.receiverFireText.setVisibility(View.VISIBLE);
            holder.receiverFireImg.setVisibility(View.GONE);
            processTextView(view, position, content, message, holder.message);
            DestructManager.getInstance().startDestruct(message.getMessage());
        }
    }

    @Override
    public void bindView(final View v, int position, TextMessage content, final UIMessage data) {
        MyTextMessageItemProvider.ViewHolder holder = (MyTextMessageItemProvider.ViewHolder) v.getTag();
        holder.receiverFire.setTag(data.getUId());

        //判断消息方向
        if (data.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_left);
            holder.message.setTextColor(Color.parseColor("#cb120f"));
            holder.message.setTextSize(25);
        } else {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_left);
            holder.message.setTextColor(R.color.rc_main_theme);
            holder.message.setTextSize(9);
        }

        if (content.isDestruct()) {
            bindFireView(v, position, content, data);
        } else {
            holder.sendFire.setVisibility(View.GONE);
            holder.receiverFire.setVisibility(View.GONE);
            holder.unRead.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
            final AutoLinkTextView textView = holder.message;
            processTextView(v, position, content, data, textView);
        }
    }

    private void processTextView(final View v, final int position, final TextMessage content, final UIMessage data, final AutoLinkTextView pTextView) {
        if (data.getContent() != null) {
            final TextMessage dataContent = (TextMessage) data.getContent();
            int len = dataContent.getContent().length();
            //文本消息过大，缓解下拉滑动时的卡顿问题
            if (v.getHandler() != null && len > 500) {
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pTextView.setText(dataContent.getContent());
                    }
                }, 50);
            } else {
                pTextView.setText(dataContent.getContent());

            }
        }
        pTextView.setMovementMethod(new LinkTextViewMovementMethod(new ILinkClickListener() {
            @Override
            public boolean onLinkClick(String link) {
                RongIM.ConversationBehaviorListener listener = RongContext.getInstance().getConversationBehaviorListener();
                RongIM.ConversationClickListener clickListener = RongContext.getInstance().getConversationClickListener();
                boolean result = false;
                if (listener != null) {
                    result = listener.onMessageLinkClick(v.getContext(), link);
                } else if (clickListener != null) {
                    result = clickListener.onMessageLinkClick(v.getContext(), link, data.getMessage());
                }
                if ((listener == null && clickListener == null) || !result) {
                    String str = link.toLowerCase();
                    if (str.startsWith("http") || str.startsWith("https")) {
                        Intent intent = new Intent(RongKitIntent.RONG_INTENT_ACTION_WEBVIEW);
                        intent.setPackage(v.getContext().getPackageName());
                        intent.putExtra("url", link);
                        v.getContext().startActivity(intent);
                        result = true;
                    }
                }
                return result;
            }
        }));
        pTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.performClick();
            }
        });
        pTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return v.performLongClick();
            }
        });
        pTextView.stripUnderlines();
    }

    private void bindFireView(View pV, int pPosition, TextMessage pContent, final UIMessage pData) {
        MyTextMessageItemProvider.ViewHolder holder = (MyTextMessageItemProvider.ViewHolder) pV.getTag();
        if (pData.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.sendFire.setVisibility(View.VISIBLE);
            holder.receiverFire.setVisibility(View.GONE);
            holder.unRead.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);

            processTextView(pV, pPosition, pContent, pData, holder.message);
        } else {
            holder.sendFire.setVisibility(View.GONE);
            holder.receiverFire.setVisibility(View.VISIBLE);
            DestructManager.getInstance().addListener(pData.getUId(), new MyTextMessageItemProvider.DestructListener(holder, pData), TAG);
            //getReadTime>0，证明已读，开始倒计时
            if (pData.getMessage().getReadTime() > 0) {
                holder.unRead.setVisibility(View.GONE);
                holder.message.setVisibility(View.VISIBLE);



                holder.receiverFireText.setVisibility(View.VISIBLE);
                String unFinishTime;
                if (TextUtils.isEmpty(pData.getUnDestructTime())) {
                    unFinishTime = DestructManager.getInstance().getUnFinishTime(pData.getUId());
                } else {
                    unFinishTime = pData.getUnDestructTime();
                }
                holder.receiverFireText.setText(unFinishTime);
                holder.receiverFireImg.setVisibility(View.GONE);
                processTextView(pV, pPosition, pContent, pData, holder.message);
                DestructManager.getInstance().startDestruct(pData.getMessage());
            } else {
                holder.unRead.setVisibility(View.VISIBLE);
                holder.message.setVisibility(View.GONE);
                holder.receiverFireText.setVisibility(View.GONE);
                holder.receiverFireImg.setVisibility(View.VISIBLE);
            }
        }
    }


    private static class DestructListener implements RongIMClient.DestructCountDownTimerListener {
        private WeakReference<MyTextMessageItemProvider.ViewHolder> mHolder;
        private UIMessage mUIMessage;

        public DestructListener(MyTextMessageItemProvider.ViewHolder pHolder, UIMessage pUIMessage) {
            mHolder = new WeakReference<>(pHolder);
            mUIMessage = pUIMessage;
        }

        @Override
        public void onTick(long millisUntilFinished, String messageId) {
            if (mUIMessage.getUId().equals(messageId)) {
                MyTextMessageItemProvider.ViewHolder viewHolder = mHolder.get();
                if (viewHolder != null && messageId.equals(viewHolder.receiverFire.getTag())) {
                    viewHolder.receiverFireText.setVisibility(View.VISIBLE);
                    viewHolder.receiverFireImg.setVisibility(View.GONE);
                    String unDestructTime = String.valueOf(Math.max(millisUntilFinished, 1));
                    viewHolder.receiverFireText.setText(unDestructTime);
                    mUIMessage.setUnDestructTime(unDestructTime);
                }
            }
        }

        @Override
        public void onStop(String messageId) {
            if (mUIMessage.getUId().equals(messageId)) {
                MyTextMessageItemProvider.ViewHolder viewHolder = mHolder.get();
                if (viewHolder != null && messageId.equals(viewHolder.receiverFire.getTag())) {
                    viewHolder.receiverFireText.setVisibility(View.GONE);
                    viewHolder.receiverFireImg.setVisibility(View.VISIBLE);
                    mUIMessage.setUnDestructTime(null);
                }
            }
        }

        public void setHolder(MyTextMessageItemProvider.ViewHolder pHolder) {
            mHolder = new WeakReference<>(pHolder);
        }

        public void setUIMessage(UIMessage pUIMessage) {
            mUIMessage = pUIMessage;
        }
    }



}
