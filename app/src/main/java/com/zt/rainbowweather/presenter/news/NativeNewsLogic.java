package com.zt.rainbowweather.presenter.news;

 import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

 import com.zt.rainbowweather.entity.advise.AdviseTitleBean;
 import com.zt.rainbowweather.entity.weather.ConventionWeather;
 import com.zt.rainbowweather.ui.fragment.IndexDetailsFragment;
 import com.zt.weather.R;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.ColumnHorizontalPackage;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.view.ColumnHorizontalScrollView;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.entity.news.NewColumn;
 import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.fragment.NativeColumnFragment;
import com.zt.rainbowweather.ui.fragment.WeatherDetailsFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * 资讯页面的逻辑
 * */
public class NativeNewsLogic {

    private static NativeNewsLogic nativeNewsLogic;
    private ArrayList<NativeColumnFragment> fragments = new ArrayList<>();
    private ArrayList<WeatherDetailsFragment> fragmentList = new ArrayList<>();
    private ArrayList<IndexDetailsFragment> fragmentIndexList = new ArrayList<>();
    private String[] ID = new String[]{"26", "28", "29", "30", "31", "32", "27", "33", "34", "35", "36", "37", "38", "40", "39", "32"};

    /*栏目数据*/
    private List<String> userColumnList = new ArrayList<>();
    private List<String> userColumnList2 = new ArrayList<>();
    private ColumnHorizontalPackage columnHorizontalPackage;

    public static NativeNewsLogic getNativeNewsLogic() {
        if (nativeNewsLogic == null) {
            synchronized (NativeNewsLogic.class){
                if (nativeNewsLogic == null) {
                    nativeNewsLogic = new NativeNewsLogic();
                }
            }
        }
        return nativeNewsLogic;
    }

    /**
     * 新闻栏目
     */
    public void RequestNewsData(AppCompatActivity context, BaseFragment fragment, LinearLayout mRadioGroupContent, ColumnHorizontalScrollView column, ViewPager viewpagerColumn) {
        NewsRequest.getNewsRequest().getNewsColumnData(context, new RequestSyntony<NewColumn>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewColumn newColumn) {
                try {
                    for (int i = 0; i < newColumn.getData().size(); i++) {
                        //传递数据到fragment
                        Bundle data = new Bundle();
                        data.putString("text", "" + newColumn.getData().get(i).getChannelid());
                        if (i == 0) {
                            SaveShare.saveValue(context, "Channelid1", newColumn.getData().get(i).getChannelid() + "");
                        }
                        NativeColumnFragment newfragment = new NativeColumnFragment();
                        newfragment.setArguments(data);
                        //加载fragment
                        fragments.add(newfragment);
                        userColumnList.add(newColumn.getData().get(i).getChannel_name());
                    }
                    columnHorizontalPackage = new ColumnHorizontalPackage<String>(context, column, viewpagerColumn);
                    columnHorizontalPackage.initData(fragment, mRadioGroupContent, fragments, userColumnList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 天气详情
     * */
    public void WeatherDetails(AppCompatActivity context, List<OutLookWeather> outLookWeathers, LinearLayout mRadioGroupContent, ColumnHorizontalScrollView column, ViewPager viewpagerColumn){
        fragmentList.clear();
        userColumnList2.clear();
        try {
            WeatherDetailsFragment.ISAD = true;
            for (int i = 0; i < outLookWeathers.size(); i++) {
                //传递数据到fragment
                Bundle data = new Bundle();
                data.putSerializable("DailyForecastBean",outLookWeathers.get(i));
                WeatherDetailsFragment newfragment = new WeatherDetailsFragment();
                newfragment.setArguments(data);
                //加载fragment
                fragmentList.add(newfragment);
    //            userColumnList.clear();
                userColumnList2.add(outLookWeathers.get(i).week +"\n"+outLookWeathers.get(i).date);
            }
            columnHorizontalPackage = new ColumnHorizontalPackage<String>(context, column, viewpagerColumn);
            columnHorizontalPackage.TextviewColor(R.color.white,R.color.white_3, com.constellation.xylibrary.R.drawable.ic_remove_w);
            columnHorizontalPackage.initData(null, mRadioGroupContent, fragmentList, userColumnList2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指数详情
     * */
    public void IndexDetails(AppCompatActivity context, List<ConventionWeather.HeWeather6Bean.LifestyleBean> lifestyleBeans, LinearLayout mRadioGroupContent, ColumnHorizontalScrollView column, ViewPager viewpagerColumn){
        fragmentList.clear();
        userColumnList2.clear();
        try {
            WeatherDetailsFragment.ISAD = true;
            for (int i = 0; i < lifestyleBeans.size(); i++) {
                //传递数据到fragment
                Bundle data = new Bundle();
                AdviseTitleBean adviseTitleBean = new AdviseTitleBean();
                adviseTitleBean.contentUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
                adviseTitleBean.headerImgUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
                adviseTitleBean.imgUrl = "http://f4.market.xiaomi.com/download/MiSafe/001a2c4210f6944e83427fd96c3216666b4de8646/a.webp";
                adviseTitleBean.title = lifestyleBeans.get(i).getBrf();
                adviseTitleBean.headerSummary = lifestyleBeans.get(i).getTxt();
                adviseTitleBean.channelId = lifestyleBeans.get(i).getType();
                adviseTitleBean.indexId = ID[i];
                data.putString("IndexSize",i+"");
                data.putSerializable("advise",adviseTitleBean);
                IndexDetailsFragment newfragment = new IndexDetailsFragment();
                newfragment.setArguments(data);
                //加载fragment
                fragmentIndexList.add(newfragment);
                //            userColumnList.clear();
                userColumnList2.add(lifestyleBeans.get(i).getType());
            }
            columnHorizontalPackage = new ColumnHorizontalPackage<String>(context, column, viewpagerColumn);
            columnHorizontalPackage.TextviewColor(R.color.white,R.color.white_3, com.constellation.xylibrary.R.drawable.ic_remove_w);
            columnHorizontalPackage.initData(null, mRadioGroupContent, fragmentIndexList, userColumnList2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
