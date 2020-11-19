package com.guozongkui.testimsdk.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guozongkui.testimsdk.R;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.widget.AutoRefreshListView;

public class CustomizedConversationFragment extends ConversationFragment {



    @Override
    public boolean showMoreClickItem() {
        return true;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }




}
