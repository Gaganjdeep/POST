package com.ariseden.post.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

import com.ariseden.post.R;
import com.ariseden.post.UtillsG.DateUtilsG;
import com.ariseden.post.activities.fragments.ImageDetailsFragment;
import com.ariseden.post.adapter.HomeModel;
import com.ariseden.post.widget.CountDownView;
import com.ariseden.post.widget.TimerListener;

public class ViewImageActivity extends BaseActivityG implements TimerListener
{

    CountDownView countdownview;

//    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_view_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Explode explode = new Explode();
            explode.setDuration(300);
            getWindow().setEnterTransition(explode);
        }


        countdownview = (CountDownView) findViewById(R.id.countdownview);
//        countdownview.setInitialTime(DateUtilsG.timeLeft(getLocaldata().getThemeEndDate()));
        countdownview.setInitialTime(DateUtilsG.getRemainingTime(getLocaldata().getMiliSecondsLeft()));

        countdownview.start();
        countdownview.setListener(this);

        ImageDetailsFragment imageDetailsFragment = new ImageDetailsFragment();
        HomeModel            mhomeModel           = (HomeModel) getIntent().getSerializableExtra("data");
        imageDetailsFragment.setData(mhomeModel);


        getSupportFragmentManager().beginTransaction().replace(R.id.container, imageDetailsFragment).commit();

//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onBackPressed()
    {
        finish();
//     super.onBackPressed();
    }


    @Override
    public void timerElapsed()
    {
        countdownview.reset();
    }

    public void hOme(View view)
    {
        finish();
    }


    private class MyPageAdapter extends FragmentPagerAdapter
    {
        public MyPageAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public int getCount()
        {
            return 1;
        }

        @Override
        public Fragment getItem(int position)
        {
            return new ImageDetailsFragment();
        }
    }


}
