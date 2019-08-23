package com.zt.rainbowweather.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.message.UmengNotifyClickActivity;
import com.umeng.message.entity.UMessage;
import com.zt.rainbowweather.BasicApplication;
import com.zt.weather.R;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MipushTestActivity extends UmengNotifyClickActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush_test);

    }
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        try {
            UMessage msg = new UMessage(new JSONObject(message));
            Map<String, String> extra = msg.extra;
            if(extra != null){
                if(extra.get("type").equals("1")){     // 其中的 “1”为你在后台设置具体跳转到那个Activity的代表标志，需要跳转到的每个 Activity 取一个不同的标志
                    Intent intent1 = new Intent(this,StartActivity.class);   // 我想跳转到 Main3Activity
                    startActivity(intent1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
