package com.example.astropagerity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class MainPagerActivity extends FragmentActivity {

	ViewPager vp;
	MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);

		adapter = new MyAdapter(getSupportFragmentManager());

		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(adapter);
		vp.setCurrentItem(0);
	}

	private class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {
			case 1:
				return new HoroFragment();
			case 2:
				return new SeasonsFragment();
			case 3:
				return new MoonPhaseFragment();
			case 4:
				return new ThousandsFragment();
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

	}

}
