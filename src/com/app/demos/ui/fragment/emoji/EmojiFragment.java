package com.app.demos.ui.fragment.emoji;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.demos.R;

/**
 * Created by tom on 15-11-15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EmojiFragment extends Fragment {
    private EditText editText;
    private String TAG = "EmojiFragment";
    private SelectFaceHelper mFaceHelper;
    private Context context;

    public EmojiFragment(){}

    @SuppressLint("ValidFragment")
    public EmojiFragment(EditText editText) {
        this.editText = editText;
    }

    public static EmojiFragment newInstance(String s, EditText editText) {
        EmojiFragment newFragment = new EmojiFragment(editText);
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        return newFragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Log.d(TAG, "TestFragment-----onCreate");
        Bundle args = getArguments();
        String hello = args != null ? args.getString("hello") : "defaultHello";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment-----onCreateView");
        View view = inflater.inflate(R.layout.send_msg_tool, container, false);
//
        if (null == mFaceHelper) {
            mFaceHelper = new SelectFaceHelper(context, view);
            mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener);
        }

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TestFragment-----onDestroy");
    }


    @Override
    public void onStop() {
        super.onStop();
        // debug memory
        //debugMemory("onStop");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getStringToServer2(Context context, EditText editText) {
        String msgStr = ParseEmojiMsgUtil.convertToMsg(editText.getText(), context);// 这里不要直接用mEditMessageEt.getText().toString();
        return EmojiParser.getInstance(context).parseEmoji(msgStr);
    }

    public String getStringToServer() {
        String msgStr = ParseEmojiMsgUtil.convertToMsg(editText.getText(), context);// 这里不要直接用mEditMessageEt.getText().toString();
        String unicode = EmojiParser.getInstance(context).parseEmoji(msgStr);
        return unicode;
    }

    SelectFaceHelper.OnFaceOprateListener mOnFaceOprateListener = new SelectFaceHelper.OnFaceOprateListener() {
        @Override
        public void onFaceSelected(SpannableString spanEmojiStr) {
            if (null != spanEmojiStr) {
                editText.append(spanEmojiStr);
                //SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(context, getStringToServer());
                //editText.append(spannableString);
            }
        }

        @Override
        public void onFaceDeleted() {
            int selection = editText.getSelectionStart();
            String text = editText.getText().toString();
            if (selection > 0) {
                String text2 = text.substring(selection - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selection;
                    editText.getText().delete(start, end);
                    return;
                }
                editText.getText().delete(selection - 1, selection);
            }
        }

    };
}
