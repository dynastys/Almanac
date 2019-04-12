package com.zt.rainbowweather.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenguang.weather.R;
import com.xy.xylibrary.utils.DensityUtil;
import com.zt.rainbowweather.entity.weather.ConventionWeather;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<ConventionWeather.HeWeather6Bean.LifestyleBean> {

        private int resourceId;
        GridView mm;
        private Context context;
        private int[] icon;
        public CustomAdapter(Context context, int resource,
                             List<ConventionWeather.HeWeather6Bean.LifestyleBean> objects, GridView gridView,int[] icon) {
            super(context, resource, objects);
            this.context = context;
            resourceId = resource;
            this.icon = icon;
            mm = gridView;
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = null;
            try {
                String functionName = getItem(position).getType();
//            int pictureSrc = getItem(position).getPictureSrc();
                LayoutInflater mInflater = LayoutInflater.from(getContext());
                view = mInflater.inflate(R.layout.function_item, null);
                TextView mTextView = (TextView) view.findViewById(R.id.function_tv);
                View mView = (View) view.findViewById(R.id.item_rel);
                ImageView mImageView = (ImageView) view
                        .findViewById(R.id.function_iv);
                mTextView.setText(functionName);
                mImageView.setImageResource(icon[position]);
                Log.e("gridviewheight", "" + mm.getHeight());
                ViewGroup.LayoutParams mLayoutParams = mView.getLayoutParams();
                mLayoutParams.height = (int) ((mm.getHeight() - DensityUtil.dip2px(
                        context, 4.0f)) / 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;

    }

}
