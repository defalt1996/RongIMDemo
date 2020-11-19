package com.guozongkui.testimsdk.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.model.LoginResult;
import com.guozongkui.testimsdk.model.Result;
import com.guozongkui.testimsdk.net.RetrofitHelper;
import com.guozongkui.testimsdk.net.RetrofitUtil;
import com.guozongkui.testimsdk.net.service.UserService;

import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    public RegisterActivity registerActivity;
    private View view;

    EditText phoneNumber;
    EditText password;
    Button btnLogin;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registerActivity = (RegisterActivity) getActivity();
        return initView();
    }

    private View initView() {

        if (view == null){
            view = View.inflate(registerActivity, R.layout.fragment_login, null);
        }
        TextView tvBtnJump2Register = view.findViewById(R.id.bt_jump2register);
        tvBtnJump2Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerActivity.switchFragment("loginFragment", "registerFragment");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneNumber = view.findViewById(R.id.editTextPhone);
        password = view.findViewById(R.id.editTextPassword);
        btnLogin = view.findViewById(R.id.bt_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {

        UserService request = RetrofitHelper.getInstance().getRetrofit(getActivity()).create(UserService.class);

        final HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("region", 86);
        paramsMap.put("phone", phoneNumber.getText().toString());
        paramsMap.put("password", password.getText().toString());
        RequestBody body = RetrofitUtil.createJsonRequest(paramsMap);

        Call<Result<LoginResult>> responseCall = request.login(body);

        responseCall.enqueue(new Callback<Result<LoginResult>>() {
            @Override
            public void onResponse(Call<Result<LoginResult>> call, Response<Result<LoginResult>> response) {
                String IMToken = response.body().getResult().token;
                String id = response.body().getResult().id;
                Log.d("Login", "onResponse: IMToken: "+ IMToken + "; id: "+ id);
                Toast.makeText(getActivity(), "Login successfully!", Toast.LENGTH_SHORT).show();

                RongIM.getInstance().setVoiceMessageType(RongIM.VoiceMessageType.Ordinary);

                RongIM.connect(IMToken, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("Connect:", "onSuccess: Connected!");
                    }

                    @Override
                    public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                        Log.d("Connect:", "onError: "+ connectionErrorCode);
                    }

                    @Override
                    public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                        Log.d("Connect:", "onDatabaseOpened: "+ databaseOpenStatus);
                    }
                });

                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<Result<LoginResult>> call, Throwable t) {

            }
        });

    }
}