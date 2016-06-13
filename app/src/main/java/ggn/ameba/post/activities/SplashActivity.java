package ggn.ameba.post.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.SharedPrefHelper;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.widget.TimerService;

public class SplashActivity extends BaseActivityG
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try
        {
            if (UtillG.isMyServiceRunning(TimerService.class, SplashActivity.this))
            {
                stopService(new Intent(SplashActivity.this, TimerService.class));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        updateThemeInfo();

//            }
//        }, 1000);


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

                        SharedPrefHelper sharedPrefHelper = getLocaldata();
                        sharedPrefHelper.setThemeName(jbojInner.getString("ThemeName"));
                        sharedPrefHelper.setThemeOverview(jbojInner.getString("Overview"));
                        sharedPrefHelper.setThemeEndDate(jbojInner.getString("ThemeEndDate"));
                        sharedPrefHelper.setThemeStartDate(jbojInner.getString("ThemeStartDate"));

                        sharedPrefHelper.setThemeID(jbojInner.getString("ThemeID"));
                        sharedPrefHelper.setMarqueeText(jbojInner.getString("MarqueeText"));

                        sharedPrefHelper.setMiliSecondsLeft(jbojInner.optString("MiliSecondsLeft"));


                        if (getLocaldata().getUserid().equals("") || !getLocaldata().isEmailVerified())
                        {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                        else
                        {
                            startActivity(new Intent(SplashActivity.this, HomeTabActivity.class));
                        }

                    }
                    else
                    {
                        UtillG.show_dialog_msg(SplashActivity.this, "Unable to fetch active THEME..,Please try again.", onClickListener);

                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), SplashActivity.this, true);
                    }

                }
                catch (Exception e)
                {
                    UtillG.show_dialog_msg(SplashActivity.this, "Unable to connect..,Please try again.", onClickListener);
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            UtillG.global_dialog.dismiss();
            updateThemeInfo();
        }
    };
}
