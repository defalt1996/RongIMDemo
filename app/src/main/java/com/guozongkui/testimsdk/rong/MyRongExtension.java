package com.guozongkui.testimsdk.rong;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import io.rong.imkit.RongExtension;

public class MyRongExtension extends RongExtension {

    EditText mEditText;

    TextView myCommonPhraseToggle;

    public MyRongExtension(Context context) {
        super(context);
        tryAddTextChangedAction();
    }

    public MyRongExtension(Context context, AttributeSet attrs) {
        super(context, attrs);


        tryAddTextChangedAction();

    }

    private void tryAddTextChangedAction() {
        mEditText = getInputEditText();
        TextWatcher textWatcher = new TextWatcher() {

            private int start;
            private int count;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void afterTextChanged(Editable s) {

                // 这块的检验规则是纯emoji的， 加其他表情的话直接给他去掉， 或者自己写规则。
//                if (QQEmoji.isQQEmoji(s.subSequence(start, start + count).toString())) {
                    mEditText.removeTextChangedListener(this);
                    String resultStr = QQEmoji.replaceEmojiWithText(s.toString());
                    mEditText.setText(QQEmoji.ensure(resultStr), TextView.BufferType.SPANNABLE);
                    mEditText.setSelection(mEditText.getText().length());
                    mEditText.addTextChangedListener(this);
//                }
            }
        };
        mEditText.addTextChangedListener(textWatcher);
    }


}
