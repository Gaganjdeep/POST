package ggn.ameba.post.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.DateUtilsG;
import ggn.ameba.post.activities.fragments.ImageDetailsFragment;
import ggn.ameba.post.adapter.HomeModel;
import ggn.ameba.post.widget.CountDownView;
import ggn.ameba.post.widget.TimerListener;

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
        countdownview.setInitialTime(DateUtilsG.timeLeft(getLocaldata().getThemeEndDate()));

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




    /* private void openwithAnim()
    {
        Bundle b = getIntent().getExtras(); // Bundle passed from previous activity to this activity

        int x  = b.getInt("X");              //b is button in previous activity
        int y  = b.getInt("Y");              //b is button in previous activity
        int xh = b.getInt("XH");            //b is button in previous activity
        int yh = b.getInt("YH");            //b is button in previous activity

        AnimationSet set = new AnimationSet(true);

        int width  = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();


        Animation scale = new ScaleAnimation((float) xh / width, 1f, (float) yh / height, 1f, x, y);

        Animation alpha = new AlphaAnimation(.75f, 1f);

        set.addAnimation(scale);
        set.addAnimation(alpha);

        set.setDuration(400);

        ((LinearLayout) findViewById(R.id.layout)).startAnimation(set);
    }*/


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
