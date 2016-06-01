package ggn.ameba.post.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isseiaoki.simplecropview.util.Utils;

import org.json.JSONObject;

import java.util.HashMap;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.DateUtilsG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.SharedPrefHelper;
import ggn.ameba.post.UtillsG.ShowIntrensicAd;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.activities.HomeTabActivity;
import ggn.ameba.post.activities.LoginActivity;
import ggn.ameba.post.activities.SplashActivity;
import ggn.ameba.post.activities.ThemeInfoActivtiy;
import ggn.ameba.post.widget.CountDownView;
import ggn.ameba.post.widget.TimerListener;

public class HomeFragmentMain extends BaseFragmentG implements TimerListener
{

    private FrameLayout frameContainer;

    CountDownView countdownview;

    private LinearLayout themeLayout;

    private TextView tvFood, tvMarquee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);

        countdownview = (CountDownView) view.findViewById(R.id.countdownview);

        frameContainer = (FrameLayout) view.findViewById(R.id.container);
        themeLayout = (LinearLayout) view.findViewById(R.id.themeLayout);
        tvFood = (TextView) view.findViewById(R.id.tvFood);
        tvMarquee = (TextView) view.findViewById(R.id.tvMarquee);

        getChildFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


        tvFood.setText(getLocaldata().getThemeName());
        themeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), ThemeInfoActivtiy.class));
            }
        });


        tvMarquee.setText(getLocaldata().getMarqueeText());
        tvMarquee.setSelected(true);



        return view;
    }

    @Override
    public void onResume()
    {
        countdownview.setInitialTime(DateUtilsG.timeLeft(getLocaldata().getThemeEndDate()));
        countdownview.start();
        countdownview.setListener(this);

        super.onResume();
    }

    @Override
    public void timerElapsed()
    {
        countdownview.reset();
        updateThemeInfo();
    }


    private void updateThemeInfo()
    {
        new SuperAsyncG(GlobalConstantsG.URL + "theme/GetActiveTheme", new HashMap<String, String>(), new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {
                        JSONObject jbojInner = new JSONObject(jboj.getString(GlobalConstantsG.Message));


                        if (getLocaldata().getThemeID().equals(jbojInner.getString("ThemeID")))
                        {
                            // TODO: THEME IS NOT UPDATED ON SERVER..
                        }
                        else
                        {
                            SharedPrefHelper sharedPrefHelper = getLocaldata();
                            sharedPrefHelper.setThemeName(jbojInner.getString("ThemeName"));
                            sharedPrefHelper.setThemeOverview(jbojInner.getString("Overview"));
                            sharedPrefHelper.setThemeEndDate(jbojInner.getString("ThemeEndDate"));
                            sharedPrefHelper.setThemeStartDate(jbojInner.getString("ThemeStartDate"));

                            sharedPrefHelper.setThemeID(jbojInner.getString("ThemeID"));
                            sharedPrefHelper.setMarqueeText(jbojInner.getString("MarqueeText"));


                            UtillG.showToast("Theme Updated..!", getActivity(), true);

                            startActivity(new Intent(getActivity(), SplashActivity.class));
                            getActivity().finish();
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


}
