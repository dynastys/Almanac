package com.zt.rainbowweather.entity.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {

    public String city;//城市
    public String tmp;//当前温度
    public String tmp_max_min;//温度差
    public String qlty;//空气质量
    public String cond_txt;//实况天气状况描述
    public String cond_code;//实况天气状况描述
    public Long time;

    public Notification(){
        super();
    }

    protected Notification(Parcel in) {
        city = in.readString();
        tmp = in.readString();
        tmp_max_min = in.readString();
        qlty = in.readString();
        cond_txt = in.readString();
        cond_code = in.readString();
        if (in.readByte() == 0) {
            time = null;
        } else {
            time = in.readLong();
        }
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(tmp);
        dest.writeString(tmp_max_min);
        dest.writeString(qlty);
        dest.writeString(cond_txt);
        dest.writeString(cond_code);
        if (time == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(time);
        }
    }
}
