package ggn.ameba.post.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.IOException;
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

public class RegisterActivity extends BaseActivityG
{
    Toolbar toolbar;
    View    mLoginFormView;
    private View mProgressView;


    private EditText edname, edemail, edpassword, edconfirmpassword;

    RoundedCornersGaganImg imgProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        settingActionBar();

        findViewbyId();

    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.arrow_white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean validation()
    {
        if (edname.getText().toString().trim().isEmpty())
        {
            UtillG.showSnacky(edname, "Enter UserName");
            return false;
        }
        else if (!isEmailValid(edemail.getText().toString().trim()))
        {
            UtillG.showSnacky(edemail, "Enter a valid email");
            return false;
        }
        else if (edpassword.getText().toString().trim().length() < 5)
        {
            UtillG.showSnacky(edemail, "Password length should be greater than 5.");
            return false;
        }

        else if (!edpassword.getText().toString().trim().equals(edconfirmpassword.getText().toString().trim()))
        {
            UtillG.showSnacky(edconfirmpassword, "Password not matched");
            return false;
        }
        else
        {
            return true;
        }

    }


    private boolean isEmailValid(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void findViewbyId()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);

        imgProfilePic = (RoundedCornersGaganImg) findViewById(R.id.imgProfilePic);

        edname = (EditText) findViewById(R.id.name);
        edemail = (EditText) findViewById(R.id.emailR);
        edpassword = (EditText) findViewById(R.id.password);
        edconfirmpassword = (EditText) findViewById(R.id.confirmpassword);

        imgProfilePic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BitmapDecoderG.selectImage(RegisterActivity.this, null);
            }
        });
    }


    String baseImage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK)
        {
            Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data, getContentResolver());
//            imgProfilePic.setImageUrlRound(RegisterActivity.this, uri.toString());


            Picasso.with(RegisterActivity.this).load(uri)
                    .transform(new CircleTransform())
                    .centerCrop()
                    .resize(270, 270)
                    .placeholder(R.mipmap.ic_default_pic_rounded)
                    .error(R.mipmap.ic_default_pic_rounded)
                    .into(imgProfilePic, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            baseImage = BitmapDecoderG.getBytesImageBitmap(RegisterActivity.this, ((BitmapDrawable) imgProfilePic.getDrawable()).getBitmap());
                        }

                        @Override
                        public void onError()
                        {

                        }
                    });


        }
    }

    public void reGisterbutton(View view)
    {
        if (validation())
        {

            showProgress();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("FirstName", edname.getText().toString().trim());
            hashMap.put("EmailId", edemail.getText().toString().trim());
            hashMap.put("Password", edpassword.getText().toString().trim());

            hashMap.put("ApplicationId", DeviceID);
            hashMap.put("DeviceType", "android");
            hashMap.put("Latitude", "0");
            hashMap.put("Longitude", "0");
            hashMap.put("MobileNo", "0");
            hashMap.put("DeviceSerialNo", "0");


            if (baseImage != null)
            {
                hashMap.put("PhotoPath", baseImage);
            }

            new SuperAsyncG(GlobalConstantsG.URL + "Customer/SaveCustomer", hashMap, new CallBackG<String>()
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

                            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(RegisterActivity.this);
                            sharedPrefHelper.setUserid(jbojInner.getString("CustomerId"));
                            sharedPrefHelper.setName(jbojInner.getString("FirstName"));
                            sharedPrefHelper.setEmail(jbojInner.getString("EmailId"));
                            sharedPrefHelper.setPhotoUrl(jbojInner.getString("PhotoPath"));


                            Intent i = new Intent(RegisterActivity.this, HomeTabActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();


                        }
                        else
                        {
                            UtillG.showToast(jboj.getString(GlobalConstantsG.Message), RegisterActivity.this, true);
                        }

                    }
                    catch (Exception e)
                    {
                        UtillG.showToast("Error occur..!", RegisterActivity.this, true);
                        e.printStackTrace();
                    }

//                    startActivity(new Intent(RegisterActivity.this, HomeTabActivity.class));
                }
            }).execute();


        }

    }


    @Override
    protected void onStart()
    {
        super.onStart();
        getRegisterationID();
    }


    GoogleCloudMessaging gcm;

    String DeviceID = "";

    public void getRegisterationID()
    {

        new AsyncTask<Object, Object, Object>()
        {
            @Override
            protected Object doInBackground(Object... params)
            {

                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(RegisterActivity.this);
                    }
                    DeviceID = gcm.register(GlobalConstantsG.SENDER_ID);


                    // try
                    msg = "Device registered, registration ID=" + DeviceID;

                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            protected void onPostExecute(Object result)
            {


//                Utills.showToast(result.toString(), LoginActivity.this, true);
            }

        }.

                execute(null, null, null);


    }


}
