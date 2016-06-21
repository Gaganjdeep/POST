package com.ariseden.post.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ariseden.post.activities.fragments.HomeFragmentMain;
import com.ariseden.post.R;

import com.ariseden.post.activities.fragments.ImageSelectionFragment;
import com.ariseden.post.activities.fragments.WallFameFragment;

public class HomeTabActivity extends AppCompatActivity
{

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    TabLayout tablayout;
   static HomeTabActivity homeTabActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        homeTabActivity=this;

        tablayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        tablayout.setupWithViewPager(mViewPager);

        tablayout.setSelectedTabIndicatorHeight(5);
        tablayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));


        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);


        for (int i = 0; i < tablayout.getTabCount(); i++)
        {
            tablayout.getTabAt(i).setIcon(selectedIcons[i]);
        }


    }

    final private int selectedIcons[] = {R.mipmap.ic_list, R.mipmap.ic_camera, R.mipmap.ic_trophy};


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {


        private Fragment[] fragments = {new HomeFragmentMain(), new ImageSelectionFragment(), new WallFameFragment()};


        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments[position];
        }

        @Override
        public int getCount()
        {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return "";
        }
    }
}
