package com.xy.xylibrary.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ColumnHorizontalScrollView extends HorizontalScrollView {
	/** 传入整体布局  */
	private View ll_content;
	/** 传入拖动栏布局 */
	private View rl_column;
	/** 屏幕宽度 */
	private int mScreenWitdh = 0;
	/** 父类的活动activity */
	private Activity activity;
	
	public ColumnHorizontalScrollView(Context context) {
		super(context);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs,
                                      int defStyle) {
		super(context, attrs, defStyle);
	}
	/** 
	 * 在拖动的时候执行
	 * */
	@Override
	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		try {
			super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
			shade_ShowOrHide();
			if(!activity.isFinishing() && ll_content !=null && rl_column !=null){
				if(ll_content.getWidth() <= mScreenWitdh){
				}
			}else{
				return;
			}
			if(paramInt1 ==0){
				return;
			}
			if(ll_content.getWidth() - paramInt1 + rl_column.getLeft() == mScreenWitdh){
				return;
			}
		} catch (Exception e) {
			System.out.println("栏目拖动错误------->");
			e.printStackTrace();
		}
	}
	/** 
	 * 传入父类布局中的资源文件
	 * */
	public void setParam(Activity activity, int mScreenWitdh, View paramView1, ImageView paramView2, ImageView paramView3 , View paramView4, View paramView5){
		this.activity = activity;
		this.mScreenWitdh = mScreenWitdh;
		ll_content = paramView1;
		rl_column = paramView5;
	}
	/** 
	 * 判断左右阴影的显示隐藏效果
	 * */
	public void shade_ShowOrHide() {
		try {
			if (!activity.isFinishing() && ll_content != null) {
				measure(0, 0);
				//如果整体宽度小于屏幕宽度的话，那左右阴影都隐藏
				if (mScreenWitdh >= getMeasuredWidth()) {
				}
			} else {
				return;
			}
			//如果滑动在最左边时候，左边阴影隐藏，右边显示
			if (getLeft() == 0) {
				return;
			}
			//如果滑动在最右边时候，左边阴影显示，右边隐藏
			if (getRight() == getMeasuredWidth() - mScreenWitdh) {
				return;
			}
			//否则，说明在中间位置，左、右阴影都显示
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
