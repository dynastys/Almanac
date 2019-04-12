package com.xy.xylibrary.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.WindowManager;

import static android.graphics.Color.WHITE;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * Dialog配置
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-26 14:32
 */

public class DialogConfig implements Parcelable {

    /**
     * 窗口内容
     */
    public int layout;
    /**
     * 窗口宽度
     */
    public int width = WindowManager.LayoutParams.WRAP_CONTENT;
    /**
     * 窗口高度
     */
    public int height = WindowManager.LayoutParams.WRAP_CONTENT;

    public int gravity = Gravity.CENTER;

    public int x;

    public int y;

    public int bgResource;

    public int radius = 4; //dp

    public int stroke = WHITE; //px

    public int strokeWidth = 1; //px

    public int solid = WHITE;
    /**
     * 自动Dismiss
     */
    public boolean dismissAuto;
    /**
     * 自动Dismiss时间
     */
    public long dismissTime = 2000;
    /**
     * 是否能够取消
     */
    public boolean cancelable = true;

    public String tag = "";


    public DialogConfig() {
        super();
    }

    protected DialogConfig(Parcel in) {
        layout = in.readInt();
        width = in.readInt();
        height = in.readInt();
        gravity = in.readInt();
        x = in.readInt();
        y = in.readInt();
        bgResource = in.readInt();
        radius = in.readInt();
        stroke = in.readInt();
        strokeWidth = in.readInt();
        solid = in.readInt();
        dismissAuto = in.readByte() != 0;
        dismissTime = in.readLong();
        cancelable = in.readByte() != 0;
        tag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(layout);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(gravity);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(bgResource);
        dest.writeInt(radius);
        dest.writeInt(stroke);
        dest.writeInt(strokeWidth);
        dest.writeInt(solid);
        dest.writeByte((byte) (dismissAuto ? 1 : 0));
        dest.writeLong(dismissTime);
        dest.writeByte((byte) (cancelable ? 1 : 0));
        dest.writeString(tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DialogConfig> CREATOR = new Creator<DialogConfig>() {
        @Override
        public DialogConfig createFromParcel(Parcel in) {
            return new DialogConfig(in);
        }

        @Override
        public DialogConfig[] newArray(int size) {
            return new DialogConfig[size];
        }
    };
}
