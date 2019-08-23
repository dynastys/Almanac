package com.xy.xylibrary.ui.activity.task;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private ImageView finishTaskHead;
    private TextView fileHeadTitle;
    private RecyclerView gradSignIn;
    private TextView listBar;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Activity getContext() {
        return SignInActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void loadViewLayout() {
        finishTaskHead = findViewById(R.id.finish_task_head);
        fileHeadTitle = findViewById(R.id.file_head_title);
        gradSignIn = findViewById(R.id.grad_sign_in);
        listBar = findViewById(R.id.list_bar);
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(SignInActivity.this);
        listBar.setLayoutParams(layoutParams);
        finishTaskHead.setOnClickListener(SignInActivity.this);
    }

    @Override
    protected void bindViews() {
        try {
            for (int i = 0; i < 15; i++) {
                list.add(i + "天");
            }
            gradSignIn.setLayoutManager(new GridLayoutManager(SignInActivity.this, 7));
            BaseAdapter baseAdapter = new BaseAdapter<>(R.layout.grad_sign_in_item, list, new BaseAdapterListener<String>() {
                @Override
                public void convertView(BaseViewHolder viewHolder, String item) {
                  if(viewHolder.getAdapterPosition() < 3){
                      ((RelativeLayout)viewHolder.getView(R.id.grad_sign_in_gold_bg)).setBackgroundResource(R.drawable.sign_in_default);
                      viewHolder.setText(R.id.grad_sign_in_tv_day,"已签");
                  }else{
                      ((RelativeLayout)viewHolder.getView(R.id.grad_sign_in_gold_bg)).setBackgroundResource(R.drawable.sign_in_activate);
                      viewHolder.setText(R.id.grad_sign_in_tv_day,(viewHolder.getAdapterPosition()+1)+"天");
                  }
                  TextView textView = viewHolder.getView(R.id.grad_sign_in_gold_add);
                  if(viewHolder.getAdapterPosition() % 5 == 0){
                      textView.setBackgroundResource(R.drawable.jifendikuai);
                      textView.setText("+100");
                  }else{
                      textView.setBackgroundResource(0);
                      textView.setText("");
                  }
                }
            });
            gradSignIn.setAdapter(baseAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
