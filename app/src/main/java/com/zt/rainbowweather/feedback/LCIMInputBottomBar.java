package com.zt.rainbowweather.feedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.leancloud.chatkit.R.id;
import cn.leancloud.chatkit.R.layout;
import cn.leancloud.chatkit.R.string;
import cn.leancloud.chatkit.event.LCIMInputBottomBarEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarRecordEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarTextEvent;
import cn.leancloud.chatkit.utils.LCIMPathUtils;
import cn.leancloud.chatkit.utils.LCIMSoftInputUtils;
import cn.leancloud.chatkit.view.LCIMRecordButton;
import cn.leancloud.chatkit.view.LCIMRecordButton.RecordEventListener;
import de.greenrobot.event.EventBus;

/**
* 聊天输入框界面
* */
public class LCIMInputBottomBar extends LinearLayout {
    private View actionBtn;
    private EditText contentEditText;
    private View sendTextBtn;
    private View voiceBtn;
    private View keyboardBtn;
    private View moreLayout;
    private LCIMRecordButton recordBtn;
    private LinearLayout actionLayout;
    private View cameraBtn;
    private View pictureBtn;
    private final int MIN_INTERVAL_SEND_MESSAGE = 1000;

    public LCIMInputBottomBar(Context context) {
        super(context);
        this.initView(context);
    }

    public LCIMInputBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    @SuppressLint("WrongConstant")
    public void hideMoreLayout() {
        this.moreLayout.setVisibility(GONE);
    }

    private void initView(Context context) {
        View.inflate(context, layout.lcim_chat_input_bottom_bar_layout, this);
        this.actionBtn = this.findViewById(id.input_bar_btn_action);
        this.contentEditText = (EditText)this.findViewById(id.input_bar_et_content);
        this.sendTextBtn = this.findViewById(id.input_bar_btn_send_text);
        this.voiceBtn = this.findViewById(id.input_bar_btn_voice);
        this.keyboardBtn = this.findViewById(id.input_bar_btn_keyboard);
        this.moreLayout = this.findViewById(id.input_bar_layout_more);
        this.recordBtn = (LCIMRecordButton)this.findViewById(id.input_bar_btn_record);
        this.actionLayout = (LinearLayout)this.findViewById(id.input_bar_layout_action);
        this.cameraBtn = this.findViewById(id.input_bar_btn_camera);
        this.pictureBtn = this.findViewById(id.input_bar_btn_picture);
        contentEditText.setHint("请在这里反馈您的问题");
        voiceBtn.setVisibility(GONE);
        cameraBtn.setVisibility(GONE);
        this.setEditTextChangeListener();
        this.initRecordBtn();
        this.actionBtn.setOnClickListener(new OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                boolean showActionView = 8 == LCIMInputBottomBar.this.moreLayout.getVisibility() || 8 == LCIMInputBottomBar.this.actionLayout.getVisibility();
                LCIMInputBottomBar.this.moreLayout.setVisibility(showActionView ? VISIBLE : GONE);
                LCIMInputBottomBar.this.actionLayout.setVisibility(showActionView ? VISIBLE : GONE);
                LCIMSoftInputUtils.hideSoftInput(LCIMInputBottomBar.this.getContext(), LCIMInputBottomBar.this.contentEditText);
            }
        });
        this.contentEditText.setOnClickListener(new OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                LCIMInputBottomBar.this.moreLayout.setVisibility(8);
                LCIMSoftInputUtils.showSoftInput(LCIMInputBottomBar.this.getContext(), LCIMInputBottomBar.this.contentEditText);
            }
        });
        this.keyboardBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LCIMInputBottomBar.this.showTextLayout();
            }
        });
        this.voiceBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LCIMInputBottomBar.this.showAudioLayout();
            }
        });
        this.sendTextBtn.setOnClickListener(new OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                String content = LCIMInputBottomBar.this.contentEditText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(LCIMInputBottomBar.this.getContext(), string.lcim_message_is_null, Toast.LENGTH_LONG).show();
                } else {
                    LCIMInputBottomBar.this.contentEditText.setText("");
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            LCIMInputBottomBar.this.sendTextBtn.setEnabled(true);
                        }
                    }, 1000L);
                    EventBus.getDefault().post(new LCIMInputBottomBarTextEvent(3, content, LCIMInputBottomBar.this.getTag()));
                }
            }
        });
        this.pictureBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(new LCIMInputBottomBarEvent(0, LCIMInputBottomBar.this.getTag()));
            }
        });
        this.cameraBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(new LCIMInputBottomBarEvent(1, LCIMInputBottomBar.this.getTag()));
            }
        });
    }

    public void addActionView(View view) {
        this.actionLayout.addView(view);
    }

    private void initRecordBtn() {
        this.recordBtn.setSavePath(LCIMPathUtils.getRecordPathByCurrentTime(this.getContext()));
        this.recordBtn.setRecordEventListener(new RecordEventListener() {
            public void onFinishedRecord(String audioPath, int secs) {
                EventBus.getDefault().post(new LCIMInputBottomBarRecordEvent(4, audioPath, secs, LCIMInputBottomBar.this.getTag()));
                LCIMInputBottomBar.this.recordBtn.setSavePath(LCIMPathUtils.getRecordPathByCurrentTime(LCIMInputBottomBar.this.getContext()));
            }

            public void onStartRecord() {
            }
        });
    }

    private void showTextLayout() {
        this.contentEditText.setVisibility(VISIBLE);
        this.recordBtn.setVisibility(GONE);
//        this.voiceBtn.setVisibility(this.contentEditText.getText().length() > 0 ? GONE : VISIBLE);
        this.sendTextBtn.setVisibility(this.contentEditText.getText().length() > 0 ? VISIBLE : GONE);
        this.keyboardBtn.setVisibility(GONE);
        this.moreLayout.setVisibility(GONE);
        this.contentEditText.requestFocus();
        LCIMSoftInputUtils.showSoftInput(this.getContext(), this.contentEditText);
    }

    private void showAudioLayout() {
        this.contentEditText.setVisibility(GONE);
        this.recordBtn.setVisibility(VISIBLE);
        this.voiceBtn.setVisibility(GONE);
        this.keyboardBtn.setVisibility(VISIBLE);
        this.moreLayout.setVisibility(GONE);
        LCIMSoftInputUtils.hideSoftInput(this.getContext(), this.contentEditText);
    }

    private void setEditTextChangeListener() {
        this.contentEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                boolean showSend = charSequence.length() > 0;
                LCIMInputBottomBar.this.keyboardBtn.setVisibility(!showSend ? VISIBLE : GONE);
                LCIMInputBottomBar.this.sendTextBtn.setVisibility(showSend ? VISIBLE : GONE);
                LCIMInputBottomBar.this.voiceBtn.setVisibility(GONE);
            }

            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
