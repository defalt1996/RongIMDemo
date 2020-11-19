package com.guozongkui.testimsdk.rong;

import android.view.KeyEvent;
import android.widget.EditText;

import com.guozongkui.testimsdk.R;

import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import io.rong.common.RLog;
import io.rong.contactcard.ContactCardPlugin;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.DestructPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

public class MyExtensionModule extends DefaultExtensionModule {

    private MyEmoticonTab myEmoticonTab;
    private EditText mEditText;
    private Stack<EditText> stack;


    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        List<IEmoticonTab> emoticonTabs =  super.getEmoticonTabs();
        MyEmoticonTab myEmoticonTab =new MyEmoticonTab();

        myEmoticonTab.setOnItemClickListener(new IEmojiItemClickListener() {
            @Override
            public void onEmojiClick(String emoji) {

                EditText editText = MyExtensionModule.this.mEditText;
                if (editText != null) {
                    int start = editText.getSelectionStart();
                    editText.getText().insert(start, emoji);
                }
            }

            @Override
            public void onDeleteClick() {
                EditText editText = MyExtensionModule.this.mEditText;
                if (editText != null) {
                    editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                }
            }
        });
        emoticonTabs.add(myEmoticonTab);
        return emoticonTabs;
    }


    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
        stack = new Stack<>();
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        super.onAttachedToExtension(extension);
        mEditText = extension.getInputEditText();
//        RLog.i(TAG, "attach " + stack.size());
        stack.push(mEditText);
    }

    @Override
    public void onDetachedFromExtension() {
//        RLog.i(TAG, "detach " + stack.size());
        if (stack.size() > 0) {
            stack.pop();
            mEditText = stack.size() > 0 ? stack.peek() : null;
        }
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        ListIterator<IPluginModule> iterator = pluginModules.listIterator();
        pluginModules.clear();
//        // 删除扩展项
//        while (iterator.hasNext()) {
//            IPluginModule integer = iterator.next();
//            // 以删除 FilePlugin 为例
//            if (integer instanceof FilePlugin) {
//                iterator.remove();
//            }
//        }

        // 增加扩展项, 以 ImagePlugin 为例
        pluginModules.add(new FilePlugin());
        pluginModules.add(new ImagePlugin());
        pluginModules.add(new DefaultLocationPlugin());
//        pluginModules.add(new VideoPlugin());
//        pluginModules.add(new AudioPlugin());
        pluginModules.add(new ContactCardPlugin());
        pluginModules.add(new DestructPlugin());
//        pluginModules.add(new LocationPlugin());


        return pluginModules;

    }
}
