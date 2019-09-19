package com.zt.rainbowweather.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zt.weather.R;


public class WeatherDetailsDialog extends Dialog {

    private Context context;
    private String title;
    private ClickListenerInterface clickListenerInterface;
    private int notes;
    private int image;
    private int type;
    public interface ClickListenerInterface {

        public void doConfirm();
        public void doCancel();
    }

    public WeatherDetailsDialog(Context context, String title, int notes,int img,int type) {
        super(context, R.style.LoadingDialogTheme);
        this.context = context;
        this.title = title;
        this.notes = notes;
        this.image = img;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.index_details_dialog, null);
        setContentView(view);
        TextView tvTitle = view.findViewById(R.id.index_title_tv);
        TextView tvConfirm = view.findViewById(R.id.index_details_tv);
        Button hint_content = view.findViewById(R.id.i_see_btn);
        ImageView index_details_img = view.findViewById(R.id.index_details_img);
//        hint_content.setText(notes);
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }else{
            tvTitle.setVisibility(View.GONE);
        }
        if(image != 0){
            index_details_img.setImageResource(image);
        }
        if(context != null && notes != 0){
            String str = "夏季制冷时，相对湿度以<font color='#56CCF6'>40%—80%</font>为宜。\n 冬季采暖时，应控制在<font color='#56CCF6'>30%—60%</font>。\n 老人和小孩适合的室内湿度为<font color='#56CCF6'>45%—50%</font>。\n 哮喘等呼吸道系统疾病的患者适宜的室内湿度在<font color='#56CCF6'>40%—50%</font>之间。";
            String str2 = "指数值3到4，一般为多云天气，此时紫外线强度较弱，预报等级为<font color='#56CCF6'>二级</font>；5到6，一般为少云天气，此时紫外线强度较强，预报等级为<font color='#56CCF6'>三级</font>；7到9，一般为晴天无云，此时紫外线强度很强，预报等级为<font color='#56CCF6'>四级</font>；达到或超过10，多为夏季晴日，紫外线强度特别强，预报等级为<font color='#56CCF6'>五级</font>。";
            switch (type){
                case 0:
                    tvConfirm.setText(Html.fromHtml(str));
                    break;
                case 1:
                    tvConfirm.setText(Html.fromHtml(str2));
                    break;
                case 2:
                    tvConfirm.setText(context.getResources().getText(notes));
                    break;
            }

        }else{
            tvConfirm.setText("加载错误");
        }
        hint_content.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.i_see_btn:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }

}
