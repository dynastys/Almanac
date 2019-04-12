package com.zt.rainbowweather.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenguang.weather.R;
import com.xy.xylibrary.utils.GlideUtil;
import com.zt.rainbowweather.entity.advise.AdviseDetailBean;
import com.zt.rainbowweather.entity.news.Article;

import java.util.List;

/**
 * Created by hcg on 2018/5/5.
 */

public class AdviseDetailAdapter extends BaseMultiItemQuickAdapter<Article.DateBean, BaseViewHolder> {
    public static final int TYPE_ONE = 1000010;
    public static final int TYPE_THREE = 1000011;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AdviseDetailAdapter( List<Article.DateBean>  data) {
        super(data);
        addItemType(TYPE_ONE, R.layout.item_advise_detail_one);
//        addItemType(TYPE_THREE, R.layout.item_advise_detail_three);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article.DateBean item) {
      bindOneData(helper, item);

    }


    private void bindOneData(BaseViewHolder helper, Article.DateBean item) {
        helper.setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_from,item.getAuthor());
        if(!TextUtils.isEmpty(item.getPublishTime())){
            helper.setText(R.id.tv_time,item.getPublishTime().substring(0,10));
        }else{
            helper.getView(R.id.tv_time).setVisibility(View.GONE);
        }
//        if(item.getImg().contains("jpg") || item.getImg().contains("png")){
            Glide.with(mContext)
                    .load(item.getImg())
                    .into((ImageView) helper.getView(R.id.iv_title));
        if(item.nativelogic != null){
            item.nativelogic.AdShow(null);
        }
//        }else{
//            GlideUtil.getGlideUtil().setGifImages(mContext,item.getImg(),(ImageView) helper.getView(R.id.iv_title));
//        }


    }
}

