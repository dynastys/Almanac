package com.xy.xylibrary.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;


import com.xy.xylibrary.base.BaseFragment;

import java.util.ArrayList;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
	private ArrayList<BaseFragment> fragments;
	private FragmentManager fm;

	public FragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}

	public FragmentPagerAdapter(FragmentManager fm,
                                ArrayList<BaseFragment> fragments) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public BaseFragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void setFragments(ArrayList<BaseFragment> fragments) {
		if (this.fragments != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (BaseFragment f : this.fragments) {
				ft.remove( f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragments = fragments;
		notifyDataSetChanged();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		Object obj = super.instantiateItem(container, position);
//		Log.e("position", "getItem: "+position );

//		hideFragments();
//		Fragment fragment = fragments.get(position);
//		FragmentTransaction ft = fm.beginTransaction();
//		//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		ft.show(fragment);
//		ft.commitAllowingStateLoss();
		return obj;
	}
	public void hideFragments() {
		FragmentTransaction ft = fm.beginTransaction();
		for (BaseFragment fragment : fragments) {
			if (fragment != null) {
				ft.hide(fragment);
			}
		}
		ft.commitAllowingStateLoss();
	}
}
