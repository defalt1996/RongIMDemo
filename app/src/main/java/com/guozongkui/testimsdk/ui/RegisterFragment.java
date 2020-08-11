package com.guozongkui.testimsdk.ui;

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
import com.guozongkui.testimsdk.model.RegisterResult;
import com.guozongkui.testimsdk.model.Result;
import com.guozongkui.testimsdk.model.VerifyResult;
import com.guozongkui.testimsdk.net.RetrofitHelper;
import com.guozongkui.testimsdk.net.RetrofitUtil;
import com.guozongkui.testimsdk.net.service.UserService;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    public RegisterActivity registerActivity;
    private View view;

    EditText phoneNumber;
    EditText password;
    EditText verifyCode;
    EditText nickName;
    Button btSendVerifyCode;
    Button btRegister;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        // Inflate the layout for this fragment
        registerActivity = (RegisterActivity) getActivity();

        return initView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phoneNumber = view.findViewById(R.id.editTextPhone);
        verifyCode = view.findViewById(R.id.et_verifycode);
        btSendVerifyCode = view.findViewById(R.id.bt_send_code);
        nickName = view.findViewById(R.id.editTextName);
        password = view.findViewById(R.id.editTextPassword);

        btSendVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkIfPhoneValid();

            }
        });
        btRegister = view.findViewById(R.id.bt_register);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private View initView() {

        if (view == null){
            view = View.inflate(registerActivity, R.layout.fragment_register, null);
        }
        TextView tvBtnBack2Login = view.findViewById(R.id.bt_back2login);
        tvBtnBack2Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerActivity.switchFragment("registerFragment", "loginFragment");
            }
        });

        return view;
    }

    private void registerUser() {


        verifyingCode();

    }

    private void checkIfPhoneValid() {

        UserService request = RetrofitHelper.getInstance().getRetrofit(getActivity()).create(UserService.class);

        final HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("region", 86);
        paramsMap.put("phone", phoneNumber.getText().toString());
        RequestBody body = RetrofitUtil.createJsonRequest(paramsMap);

        Call<Result<Boolean>> responseCall = request.checkPhoneAvailable(body);

        responseCall.enqueue(new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (!response.body().getResult().booleanValue()){
                    Toast.makeText(registerActivity, "Phone number invalid!(Maybe registered already, pls login directly)", Toast.LENGTH_SHORT).show();
                }else {
                    sendVerifyCode();
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {

            }
        });

    }

    private void register(String verification_token) {
        UserService request = RetrofitHelper.getInstance().getRetrofit(getActivity()).create(UserService.class);

        final HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("nickname", nickName.getText().toString());
        paramsMap.put("password", password.getText().toString());
        paramsMap.put("verification_token", verification_token);
        RequestBody body = RetrofitUtil.createJsonRequest(paramsMap);

        Call<Result<RegisterResult>> responseCall = request.register(body);

        responseCall.enqueue(new Callback<Result<RegisterResult>>() {
            @Override
            public void onResponse(Call<Result<RegisterResult>> call, Response<Result<RegisterResult>> response) {

                if (response.body() != null){
                    int code = response.body().code;
                    Log.d("register:", "onResponse: code:" + code);

                }else {
                    Log.d("register:", "onResponse: error");
                }
                Toast.makeText(registerActivity, "Register successfully, pls login!", Toast.LENGTH_SHORT).show();
                registerActivity.switchFragment("registerFragment", "loginFragment");
            }

            @Override
            public void onFailure(Call<Result<RegisterResult>> call, Throwable t) {

            }
        });
    }



    private void verifyingCode() {

        UserService request = RetrofitHelper.getInstance().getRetrofit(getActivity()).create(UserService.class);

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("region", 86);
        paramsMap.put("phone", phoneNumber.getText().toString());
        paramsMap.put("code", verifyCode.getText().toString());
        RequestBody body = RetrofitUtil.createJsonRequest(paramsMap);

        Call<Result<VerifyResult>> responseCall = request.verifyCode(body);

        responseCall.enqueue(new Callback<Result<VerifyResult>>() {
            @Override
            public void onResponse(Call<Result<VerifyResult>> call, Response<Result<VerifyResult>> response) {
                String verification_token = response.body().getResult().verification_token;
                Log.d("Verification code", "onResponse: verification_token:"+ verification_token);
                register(verification_token);
            }

            @Override
            public void onFailure(Call<Result<VerifyResult>> call, Throwable t) {

            }
        });
    }

    private void sendVerifyCode() {

        UserService request = RetrofitHelper.getInstance().getRetrofit(getActivity()).create(UserService.class);

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("region", 86);
        paramsMap.put("phone", phoneNumber.getText().toString());
        RequestBody body = RetrofitUtil.createJsonRequest(paramsMap);

        Call<Result<String>> responseCall = request.sendCode(body);

        responseCall.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                int code = response.body().getCode();
                Toast.makeText(registerActivity, "Verification code sent, pls check your phone", Toast.LENGTH_SHORT).show();
                Log.d("Send verify code", "response: "+ code);
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {

            }
        });
    }
}