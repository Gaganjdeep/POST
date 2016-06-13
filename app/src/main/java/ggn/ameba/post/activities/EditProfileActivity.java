package ggn.ameba.post.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.BitmapDecoderG;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.SharedPrefHelper;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.widget.CircleTransform;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

public class EditProfileActivity extends BaseActivityG
{
    private RoundedCornersGaganImg imgProfilePic;
    private EditText               edname, edemail, edpassword, edconfirmpassword, edTagline;


    private CheckBox chkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        settingActionBar();
        findViewbyId();


    }


    private void showData()
    {
        imgProfilePic.setImageUrlRound(EditProfileActivity.this, getLocaldata().getPhotoUrl());
        edname.setText(getLocaldata().getName());

        edemail.setText(getLocaldata().getEmail());




        if (!getLocaldata().getTagLine().equals("null") && !getLocaldata().getTagLine().isEmpty())
        {
            edTagline.setText(getLocaldata().getTagLine());
        }



    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.arrow_white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    private void findViewbyId()
    {

        imgProfilePic = (RoundedCornersGaganImg) findViewById(R.id.imgProfilePic);

        chkbox = (CheckBox) findViewById(R.id.chkbox);

        edname = (EditText) findViewById(R.id.edname);
        edemail = (EditText) findViewById(R.id.edemail);
        edTagline = (EditText) findViewById(R.id.edTagline);

        edpassword = (EditText) findViewById(R.id.edpassword);
        edconfirmpassword = (EditText) findViewById(R.id.edconfirmpassword);

        imgProfilePic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BitmapDecoderG.selectImage(EditProfileActivity.this, null);
            }
        });


        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {

                edpassword.setEnabled(b);
                edconfirmpassword.setEnabled(b);

            }
        });
        chkbox.setChecked(false);

        showData();
    }


    private String baseImage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK)
        {
            Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data, getContentResolver());
//            imgProfilePic.setImageUrlRound(EditProfileActivity.this, uri.toString());


            Picasso.with(EditProfileActivity.this).load(uri)
                    .transform(new CircleTransform())
                    .centerInside()
                    .resize(270, 270)
                    .placeholder(R.mipmap.ic_default_pic_rounded)
                    .error(R.mipmap.ic_default_pic_rounded)
                    .into(imgProfilePic, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            baseImage = BitmapDecoderG.getBytesImageBitmap(EditProfileActivity.this, ((BitmapDrawable) imgProfilePic.getDrawable()).getBitmap());
                        }

                        @Override
                        public void onError()
                        {

                        }
                    });


//            Glide.with(this).load(uri)
//                    .asBitmap().
//                    into(new SimpleTarget<Bitmap>(200, 500)
//                    {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)
//                        {
//
//                            CircleTransform circleTransform=new CircleTransform();
//
//                            circleTransform.transform(resource);
//
//                            imgView.setImageBitmap(resource); // Possibly runOnUiThread()
//                            imageSet = true;
//                            displayLocation();
//                        }
//                    });


//            baseImage = BitmapDecoderG.getBytesImage(EditProfileActivity.this, uri);
        }
    }

    private boolean isEmailValid(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validation()
    {
        if (edname.getText().toString().isEmpty())
        {
            UtillG.showSnacky(edemail, "Please enter name.");
            return false;
        }
        else if (!isEmailValid(edemail.getText().toString().trim()))
        {
            UtillG.showSnacky(edemail, "Email not valid..!");
            return false;
        }
        else if (chkbox.isChecked())
        {
            if (edpassword.getText().toString().length() < 4)
            {
                UtillG.showSnacky(edconfirmpassword, "Password length should be > 4.");
                return false;
            }
            else if (!edpassword.getText().toString().equals(edconfirmpassword.getText().toString()))
            {
                UtillG.showSnacky(edconfirmpassword, "Password not matched..!");
                return false;
            }
        }

        return true;

    }


    public void updateProfile(View view)
    {
//        URL : http://112.196.34.42:8089/Customer/UpdateProfile
//        Body :
//        {"CustomerId" : 12, "FirstName" : "abc","PhotoPath": "base64" "EmailId" : "acb@gmail.com", "Password" :
//
//            "123456"}

        if (!validation())
        {
            return;
        }


        showProgress();


//            {EmailID : "acb@gmail.com", Password : "123456"}
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("CustomerId", getLocaldata().getUserid());
        hashMap.put("FirstName", edname.getText().toString());
        hashMap.put("EmailId", edemail.getText().toString());
        hashMap.put("StatusLine", edTagline.getText().toString());


        if (baseImage != null)
        {
            hashMap.put("PhotoPath", baseImage);
        }


        if (chkbox.isChecked())
        {
            hashMap.put("Password", edpassword.getText().toString());
        }

        new SuperAsyncG(GlobalConstantsG.URL + "Customer/UpdateProfile", hashMap, new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                cancelProgress();

                try
                {
                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {
                        JSONObject jbojInner = new JSONObject(jboj.getString(GlobalConstantsG.Message));

                        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(EditProfileActivity.this);
                        sharedPrefHelper.setUserid(jbojInner.getString("CustomerId"));
                        sharedPrefHelper.setName(jbojInner.getString("FirstName"));
                        sharedPrefHelper.setEmail(jbojInner.getString("EmailId"));
                        sharedPrefHelper.setPhotoUrl(jbojInner.getString("PhotoPath"));

                        sharedPrefHelper.setTagLine(jbojInner.getString("StatusLine"));

                        onBackPressed();

                    }
                    else
                    {
                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), EditProfileActivity.this, true);
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
