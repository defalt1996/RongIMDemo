package com.guozongkui.testimsdk.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.guozongkui.testimsdk.R;

public class RegisterActivity extends AppCompatActivity {


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();
        mFragmentTransaction.add(R.id.fl_main, loginFragment, "loginFragment")
                .add(R.id.fl_main, registerFragment, "registerFragment")
                .hide(registerFragment).commit();





    }

    public void switchFragment(String fromTag, String toTag) {
        Fragment from = mFragmentManager.findFragmentByTag(fromTag);
        Fragment to = mFragmentManager.findFragmentByTag(toTag);
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!to.isAdded()) {//判断是否被添加到了Activity里面去了
                transaction.hide(from).add(R.id.fl_main, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }

    }





}
