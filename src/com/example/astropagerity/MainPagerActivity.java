package com.example.astropagerity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainPagerActivity extends FragmentActivity {

	ViewPager vp;
	MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);

		adapter = new MyAdapter(getSupportFragmentManager());

		vp = (ViewPager) findViewById(R.id.pager);
		vp.setAdapter(adapter);

	}

	private class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {
			case 0:
				return new SeasonsFragment();
			case 1:
				return new ThousandsFragment();
			case 2:
				return new MoonPhaseFragment();
//			case 3:
//				return new HoroFragment();
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

}
