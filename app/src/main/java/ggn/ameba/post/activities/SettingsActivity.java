package ggn.ameba.post.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

public class SettingsActivity extends BaseActivityG
{


    private ImageView imgedit, img_eye;

    private RoundedCornersGaganImg imgProfile;
    private TextView               tvName, tvEmail, tvStatusLine;

    private View viewIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        tvStatusLine.setText(getLocaldata().getTagLine());

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


               UtillG. transitionToActivity(SettingsActivity.this, new Intent(SettingsActivity.this,EditProfileActivity.class), imgProfile, "img");
            }
        });
    }


    public void baCk(View view)
    {
        finish();
    }

    public void viewPost(View view)
    {
        startActivity(new Intent(SettingsActivity.this, ViewPostActivity.class));
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

                                Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

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
}
