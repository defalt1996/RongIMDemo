package com.guozongkui.testimsdk.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.network.HttpService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.sealtalk.im") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        HttpService request = retrofit.create(HttpService.class);

        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<GetTokenResponse> call = request.("I love you");

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<>() {


            @Override
            public void onResponse(Call<GetTokenResponse> call, Response response) {

            }

            @Override
            public void onFailure(Call<GetTokenResponse> call, Throwable t) {

            }

        });
    }


    }
}
