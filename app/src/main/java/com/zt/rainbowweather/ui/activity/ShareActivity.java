package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Shares;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.weather.R;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.share_image)
    ImageView shareImage;
    @BindView(R.id.share_rel)
    RelativeLayout shareRel;
    @BindView(R.id.share_btn)
    Button shareBtn;

    private static Uri bitmaps;

    public static void startActivity(Context context, Uri bitmap) {
        Intent intent = new Intent(context, ShareActivity.class);
//        Bundle b = new Bundle();
//        b.putParcelable("bitmap", bitmap);
//        intent.putExtras(b);
        bitmaps = bitmap;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Activity getContext() {
        return ShareActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_share;
    }

    @Override
    protected void loadViewLayout() {
//        Bitmap bmp = (Bitmap) getIntent().getExtras().getParcelable("bitmap");
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(ShareActivity.this);
        listBar.setLayoutParams(layoutParams);
        finishFileHead.setVisibility(View.VISIBLE);
        fileCoOption.setVisibility(View.GONE);
        fileHeadTitle.setText("分享也是一种陪伴 ");
        shareImage.setImageURI(bitmaps);
    }

    @Override
    protected void bindViews() {

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

    @OnClick({R.id.finish_file_head, R.id.share_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish_file_head:
                finish();
                break;
            case R.id.share_btn:
                Shares.Share(ShareActivity.this, bitmaps);
                break;
        }
    }
}
