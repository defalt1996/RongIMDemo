package com.guozongkui.testimsdk.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.guozongkui.testimsdk.R;

import io.rong.imkit.fragment.SubConversationListFragment;

public class SubConversationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_conversation_list);

        SubConversationListFragment subconversationListFragment=new SubConversationListFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, subconversationListFragment);
        transaction.commit();
    }
}