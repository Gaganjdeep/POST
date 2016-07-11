package com.ariseden.post.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariseden.post.UtillsG.CallBackG;
import com.ariseden.post.UtillsG.GlobalConstantsG;
import com.ariseden.post.UtillsG.UtillG;
import com.ariseden.post.WebService.SuperAsyncG;
import com.ariseden.post.adapter.IdCardModel;
import com.ariseden.post.widget.RoundedCornersGaganImg;

import org.json.JSONObject;

import java.util.HashMap;

import com.ariseden.post.R;

public class SettingsActivity extends BaseActivityG
{


    private ImageView imgedit, img_eye;

    private RoundedCornersGaganImg imgProfile;
    private TextView               tvName, tvEmail, tvStatusLine;

    private View viewIdCard;


    private IdCardModel idCardModel;

    private SwitchCompat      notification_switch;
    private AppCompatCheckBox notification_chkbox;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        notification_switch = (SwitchCompat) findViewById(R.id.notification_switch);

        notification_switch.setChecked(getLocaldata().isshowlocation());


        notification_chkbox = (AppCompatCheckBox) findViewById(R.id.notification_chkbox);

        notification_chkbox.setChecked(getLocaldata().isNotifing());


        notification_chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                getLocaldata().setisNotifing(b);
            }
        });


        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                getLocaldata().setshowlocation(b);
            }
        });


    }


    @Override
    protected void onResume()
    {
        showIDcard();
        super.onResume();
    }

    private void showIDcard()
    {

        viewIdCard = findViewById(R.id.viewIdCard);

        imgProfile = (RoundedCornersGaganImg) viewIdCard.findViewById(R.id.imgProfile);
        imgProfile.setImageUrlRound(SettingsActivity.this, getLocaldata().getPhotoUrl());

        tvName = (TextView) viewIdCard.findViewById(R.id.tvName);
        tvName.setText(getLocaldata().getName());

        tvStatusLine = (TextView) viewIdCard.findViewById(R.id.tvStatusLine);
        tvStatusLine.setVisibility(View.VISIBLE);

        if (!getLocaldata().getTagLine().equals("null") && !getLocaldata().getTagLine().isEmpty())
        {
            tvStatusLine.setText(getLocaldata().getTagLine());
        }


        tvEmail = (TextView) viewIdCard.findViewById(R.id.tvEmail);
        tvEmail.setVisibility(View.VISIBLE);
        tvEmail.setText(getLocaldata().getEmail());

        TextView tv = (TextView) viewIdCard.findViewById(R.id.tvPostId);
        tv.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Gnawhard.otf"));
        img_eye = (ImageView) viewIdCard.findViewById(R.id.img_eye);
        img_eye.setVisibility(View.INVISIBLE);
        imgedit = (ImageView) viewIdCard.findViewById(R.id.imgedit);
        imgedit.setImageResource(R.mipmap.ic_edit_black);
        imgedit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));


                UtillG.transitionToActivity(SettingsActivity.this, new Intent(SettingsActivity.this, EditProfileActivity.class), imgProfile, "img");
            }
        });
    }


    public void baCk(View view)
    {
        finish();
    }

    public void viewPost(View view)
    {

        idCardModel = new IdCardModel(getLocaldata().getPhotoUrl(), getLocaldata().getName(), getLocaldata().getEmail(), getLocaldata().getTagLine(), getLocaldata().getUserid());
        Intent intent = new Intent(SettingsActivity.this, ViewPostActivity.class);
        intent.putExtra("data", idCardModel);
        startActivity(intent);
    }

    public void logOut(View view)
    {
        UtillG.show_dialog_msg(SettingsActivity.this, "Are you sure,You want to logout ?", new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                showProgress();

//                URL : http://112.196.34.42:8089/Customer/Logout
//                Method : POST
//                Body :
//                {"CustomerId":123}

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("CustomerId", getLocaldata().getUserid());

                new SuperAsyncG(GlobalConstantsG.URL + "Customer/Logout", hashMap, new CallBackG<String>()
                {
                    @Override
                    public void callBack(String response)
                    {
                        try
                        {
                            cancelProgress();
                            JSONObject jboj = new JSONObject(response);

                            if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                            {
                                getLocaldata().logOut();

                                UtillG.global_dialog.dismiss();

                                Intent i = new Intent(SettingsActivity.this, SplashActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                HomeTabActivity.homeTabActivity.finish();
                                finish();
                            }
                            else
                            {
                                UtillG.showToast(jboj.getString(GlobalConstantsG.Message), SettingsActivity.this, true);
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).execute();


            }
        });
    }

    public void blockList(View view)
    {
        Intent i = new Intent(SettingsActivity.this, BlockListActivity.class);
        startActivity(i);
    }
}
