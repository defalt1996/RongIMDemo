package com.guozongkui.testimsdk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String MY_TOKEN = "O8nJqMyZc9DZxEac/QvSAh3l79AWIO/jGmsOVRo/cbY=@3deq.cn.rongnav.com;3deq.cn.rongcfg.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Boolean> supportedConversation = new HashMap<>();
                supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false);
                RongIM.getInstance().startConversationList(MainActivity.this , supportedConversation);
            }
        });

    }
}