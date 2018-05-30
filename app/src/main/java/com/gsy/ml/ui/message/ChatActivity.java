package com.gsy.ml.ui.message;

import android.text.TextUtils;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityChatBinding;
import com.gsy.ml.ui.common.BaseActivity;
import com.hyphenate.easeui.EaseConstant;

public class ChatActivity extends BaseActivity {
    private EaseChatFragment chatFragment;
    String toChatUsername;
    private ActivityChatBinding mBinding;


    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityChatBinding) vdb;

        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);

        chatFragment = new EaseChatFragment();
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        if (TextUtils.isEmpty(toChatUsername)) {//环信账号为空
            Toast.makeText(aty, "信息错误!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }
}
