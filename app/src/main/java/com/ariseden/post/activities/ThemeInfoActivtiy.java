package com.ariseden.post.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.ariseden.post.UtillsG.DateUtilsG;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.ariseden.post.R;

public class ThemeInfoActivtiy extends BaseActivityG
{

    private TextView tvThemeName, tvEndDate, tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_info_activtiy);

        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvThemeName = (TextView) findViewById(R.id.tvThemeName);
        tvOverview = (TextView) findViewById(R.id.tvOverview);

        showDate();
    }

    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,hh:mm:ss a", Locale.US);

    private void showDate()
    {
        try
        {
            String date = getLocaldata().getThemeEndDate();
            if (date.contains("."))
            {
                date = date.substring(0, date.indexOf("."));
            }

            String created_at = sdf.format(DateUtilsG.dateServer(date));


            tvThemeName.setText("Theme Name : " + getLocaldata().getThemeName());
            tvEndDate.setText("End Date : " + created_at + " (EST Time)");

            tvOverview.setText("Overview : " + getLocaldata().getThemeOverview());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
