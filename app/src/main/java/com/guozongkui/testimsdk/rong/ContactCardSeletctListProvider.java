package com.guozongkui.testimsdk.rong;

import androidx.fragment.app.Fragment;

import io.rong.contactcard.IContactCardSelectListProvider;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class ContactCardSeletctListProvider implements IContactCardSelectListProvider {
    @Override
    public void onContactPluginClick(int requestCode, Fragment currentFragment, RongExtension extension, IPluginModule pluginModule) {

    }
}
