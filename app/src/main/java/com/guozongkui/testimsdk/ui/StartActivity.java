package com.guozongkui.testimsdk.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.adapter.StartChooseUserListAdapter;
import com.guozongkui.testimsdk.model.MyUser;
import com.guozongkui.testimsdk.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    private RadioGroup radioGroup;
    private RadioButton rbDerrick, rbQin;
    RecyclerView recyclerView;

    List<MyUser> myUserList;
    StartChooseUserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        radioGroup = findViewById(R.id.rg_resource);
        rbDerrick = findViewById(R.id.rb_derrick);
        rbQin = findViewById(R.id.rb_qin);

        recyclerView = findViewById(R.id.my_user_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setUserList("derrick_resource.json");
        rbDerrick.setChecked(true);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_derrick:
                        // 获取本地数据
                        setUserList("derrick_resource.json");


                        Log.d(TAG, "onCheckedChanged: user resource : derrick");
                    case R.id.rb_qin:
                        setUserList("qin_resource.json");
                }
            }
        });

    }

    private void setUserList(String resource) {
        String userData = Utils.getLocalJson(StartActivity.this, resource);
        Gson gson = new Gson();
        myUserList = gson.fromJson(userData, new TypeToken<List<MyUser>>(){}.getType());
        adapter = new StartChooseUserListAdapter(StartActivity.this, myUserList);
        recyclerView.setAdapter(adapter);
    }
}