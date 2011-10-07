package com.nextgen.bemore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventListFragmentPagerAdapter extends FragmentPagerAdapter {
	protected static final String[] CONTENT = new String[] { "Movies", "Series", "Sport", "Documentary", "Youth"};
	
	private int mCount = CONTENT.length;

	public EventListFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return EventListFragment.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public int getCount() {
		return mCount;
	}
	
	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}