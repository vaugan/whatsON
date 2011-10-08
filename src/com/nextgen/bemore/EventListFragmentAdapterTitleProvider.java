package com.nextgen.bemore;

import android.support.v4.app.FragmentManager;
import com.viewpagerindicator.TitleProvider;

public class EventListFragmentAdapterTitleProvider extends EventListFragmentPagerAdapter implements TitleProvider {
	public EventListFragmentAdapterTitleProvider(FragmentManager fm) {
		super(fm);
	}

	public String getTitle(int position) {
		return EventListFragmentPagerAdapter.EVENT_CATEGORIES[position % EVENT_CATEGORIES.length];
	}
}