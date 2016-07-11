package com.ariseden.post.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.ariseden.post.UtillsG.BitmapDecoderG;
import com.ariseden.post.UtillsG.GlobalConstantsG;
import com.ariseden.post.WebService.SuperAsyncG;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.model.LatLng;
import com.isseiaoki.simplecropview.CropImageView;

import org.json.JSONObject;

import java.util.HashMap;

import gagan.ameba.mylocationg.CurrentLocActivityG;

import com.ariseden.post.R;

import com.ariseden.post.UtillsG.CallBackG;
import com.ariseden.post.UtillsG.SharedPrefHelper;
import com.ariseden.post.UtillsG.UtillG;

public class ImagePostActivity extends CurrentLocActivityG
{


    Location loc;

    TextView tvLocation;

    EditText edHashTag;


    @Override
    public void getCurrentLocationG(Location location)
    {
        if (imageSet)
        {
            try
            {
                loc = location;

                if (tvLocation != null)
                {
                    tvLocation.setText(UtillG.locationName(ImagePostActivity.this, new LatLng(location.getLatitude(), location.getLongitude())));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

    CropImageView imgView;


    Uri imageUri;


    Dialog dialog;


    public void showProgress()
    {
        dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
    }

    public void cancelProgress()
    {
        if (dialog != null)
        {
            dialog.cancel();
        }
    }


    boolean imageSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);

        imageUri = getIntent().getParcelableExtra("image");

        settingActionBar();


        imgView = (CropImageView) findViewById(R.id.imgView);
        imgView.setCropMode(CropImageView.CropMode.RATIO_4_3);
        imgView.setCustomRatio(4, 3);
//        imgView.setAspectRatio(1, 1);


        tvLocation = (TextView) findViewById(R.id.tvLocation);
        edHashTag = (EditText) findViewById(R.id.edHashTag);

//        imgView.setScaleEnabled(true);


//        if (imageUri.toString().contains("file://"))
//        {
//            imgView.setImageFilePath(imageUri.toString());
//        }
//        else
//        {
//            imgView.setImageFilePath(RealPathUtil.getRealPathFromURI(ImagePostActivity.this, imageUri));
//        }


//        try
//        {
//
//            File f = new File(imageUri.getPath());
//
//            imgView.setImageBitmap(BitmapDecoderG.decodeFile(f.getAbsolutePath()));
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

      /*  Picasso.with(this).load(imageUri)
                .centerInside()
                .resize(640, 640)
                .into(imgView, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        imgView.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                imgView.setImageBitmap(((BitmapDrawable) imgView.getDrawable()).getBitmap());
                                imageSet = true;
                                displayLocation();
                            }
                        });


                    }

                    @Override
                    public void onError()
                    {

                    }
                });*/


        Glide.with(this).load(imageUri).asBitmap().
                into(new SimpleTarget<Bitmap>(250, 250)
                {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)
                    {

                        imgView.setImageBitmap(resource); // Possibly runOnUiThread()
//                        imgView.setImageBitmap(((BitmapDrawable) imgView.getDrawable()).getBitmap()); // Possibly runOnUiThread()
                        imageSet = true;
                        displayLocation();
                    }
                });




     /*   Picasso.with(this).load(imageUri)
                .centerInside()
                .resize(640, 400)
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        imgView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable)
                    {
                        finish();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable)
                    {

                    }
                });*/


    }


//    private boolean isPossibleCrop(int widthRatio, int heightRatio)
//    {
//        int bitmapWidth  = imgView.getViewBitmap().getWidth();
//        int bitmapHeight = imgView.getViewBitmap().getHeight();
//        if (bitmapWidth < widthRatio && bitmapHeight < heightRatio)
//        {
//            return false;
//        }
//        else
//        {
//            return true;
//        }
//    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Post Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.arrow_white);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        if (item.getItemId() == R.id.done)
        {
            postImage();
//            finish();
        }
        else
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    String base64;

    private void postImage()
    {
        try
        {

//            base64 = BitmapDecoderG.getBytesImageBitmap(ImagePostActivity.this, ((BitmapDrawable) imgView.getDrawable()).getBitmap());
            base64 = BitmapDecoderG.getBytesImageBitmap(ImagePostActivity.this, imgView.getCroppedBitmap());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }


        showProgress();


        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(ImagePostActivity.this);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ThemeID", sharedPrefHelper.getThemeID());
        hashMap.put("CustomerId", sharedPrefHelper.getUserid());
        hashMap.put("ImagePath", base64);


        hashMap.put("HashTag", edHashTag.getText().toString());


        if (sharedPrefHelper.isshowlocation())
        {
            if (loc != null)
            {
                hashMap.put("TagLocation", tvLocation.getText().toString());
                hashMap.put("Latitude", loc.getLatitude() + "");
                hashMap.put("Longitude", loc.getLongitude() + "");
            }
            else
            {
                displayLocation();

            }
        }
        else
        {
            hashMap.put("TagLocation", "Unknown Location");
        }

        Log.e("map", "" + hashMap);


//        {"ThemeID" : 38, "CustomerId" : 123, "ImagePath" : "base64","HashTag" : "Pizza,Beer",
//                "TagLocation" : "Chandigarh", "Latitude" : 32.00, "Longitude" : 70.00

        new SuperAsyncG(GlobalConstantsG.URL + "theme/UploadThemePost", hashMap, new CallBackG<String>()
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
                        Intent updateUnreadmsg = new Intent(GlobalConstantsG.BROADCAST_UPDATE_HOME);
                        sendBroadcast(updateUnreadmsg);

                        UtillG.showToast("Image posted successfully", ImagePostActivity.this, true);
                        finish();
                    }
                    else
                    {

                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), ImagePostActivity.this, true);
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
