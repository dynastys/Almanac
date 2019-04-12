package com.zt.rainbowweather.entity.advise;

import com.chad.library.adapter.base.entity.MultiItemEntity;
 import com.zt.rainbowweather.ui.adapter.AdviseDetailAdapter;

import java.util.List;

/**
 * Created by hcg on 2018/5/5.
 */

public class AdviseDetailBean {


    public String channelId;
    public String layoutId;
    public String status;
    public String testName;
    public List<CardsBean> cards;

    public static class CardsBean {


        public String cardId;
        public CornerMarkBean cornerMark;
        public String isVisible;
        public String statKey;
        public String summary;
        public String template;
        public String title;
        public List<ListBean> list;

        public static class CornerMarkBean {


            public CornerDataBean cornerData;
            public String type;

            public static class CornerDataBean {


                public String imgUrl;
                public LinkBean link;
                public String text;

                public static class LinkBean {


                    public String appUrl;
                    public String channelId;
                    public String packageName;
                    public String type;
                    public String url;
                    public String useSystemBrowser;
                }
            }
        }

        public static class ListBean implements MultiItemEntity {


            public DataBean data;
            public String rowType;
            public String template;
            public String type;

            @Override
            public int getItemType() {
                if (data.wtrImges.size() == 1) {
                    return AdviseDetailAdapter.TYPE_ONE;
                } else {
                    return AdviseDetailAdapter.TYPE_THREE;
                }
            }

            public static class DataBean {


                public WtrCornerMarkBean wtrCornerMark;
                public String wtrDataId;
                public WtrHeadDataBean wtrHeadData;
                public WtrLinkBean wtrLink;
                public String wtrPublishTime;
                public String wtrResource;
                public String wtrStatKey;
                public String wtrSummary;
                public String wtrTitle;
                public List<String> wtrImges;

                public static class WtrCornerMarkBean {


                    public CornerDataBeanX cornerData;
                    public String type;

                    public static class CornerDataBeanX {


                        public String imgUrl;
                        public LinkBeanX link;
                        public String text;

                        public static class LinkBeanX {


                            public String appUrl;
                            public String channelId;
                            public String packageName;
                            public String type;
                            public String url;
                            public String useSystemBrowser;
                        }
                    }
                }

                public static class WtrHeadDataBean {


                    public String iconImgUrl;
                    public String imgUrl;
                    public String reserved;
                    public String summary;
                    public String template;
                    public String title;
                    public String type;
                }

                public static class WtrLinkBean {


                    public String appUrl;
                    public String channelId;
                    public String packageName;
                    public String type;
                    public String url;
                    public String useSystemBrowser;
                }
            }
        }
    }
}
