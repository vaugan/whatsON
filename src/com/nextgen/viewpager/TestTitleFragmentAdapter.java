package com.nextgen.viewpager;

import android.support.v4.app.FragmentManager;
import com.viewpagerindicator.TitleProvider;

public class TestTitleFragmentAdapter extends TestFragmentAdapter implements TitleProvider {
	public TestTitleFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public String getTitle(int position) {
		return TestFragmentAdapter.CONTENT[position % CONTENT.length];
	}
}