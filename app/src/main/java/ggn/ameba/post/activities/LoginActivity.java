package ggn.ameba.post.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.SharedPrefHelper;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivityG
{
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View     mProgressView;
    private View     mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin()
    {

        // Store values at the time of the login attempt.
        String email    = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel    = false;
        View    focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password))
        {
            UtillG.showSnacky(mEmailView, "Password not valid..!");
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!isEmailValid(email))
        {
//            mEmailView.setError("Email not valid");
            UtillG.showSnacky(mEmailView, "Email not valid..!");
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
//            showProgress(true);

            showProgress();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("EmailId", mEmailView.getText().toString().trim());
            hashMap.put("Password", mPasswordView.getText().toString().trim());
            hashMap.put("ApplicationId", DeviceID);
            hashMap.put("DeviceType", "android");

            new SuperAsyncG(GlobalConstantsG.URL + "Customer/ValidateUserCustomer", hashMap, new CallBackG<String>()
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

                            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(LoginActivity.this);
                            sharedPrefHelper.setUserid(jbojInner.getString("CustomerId"));
                            sharedPrefHelper.setName(jbojInner.getString("FirstName"));
                            sharedPrefHelper.setEmail(jbojInner.getString("EmailId"));
                            sharedPrefHelper.setPhotoUrl(jbojInner.getString("PhotoPath"));

                            sharedPrefHelper.setTagLine(jbojInner.getString("StatusLine"));

                            startActivity(new Intent(LoginActivity.this, HomeTabActivity.class));
                            finish();
                        }
                        else
                        {
                            UtillG.showToast(jboj.getString(GlobalConstantsG.Message), LoginActivity.this, true);
                        }

                    }
                    catch (Exception e)
                    {
                        cancelProgress();
                        e.printStackTrace();
                    }
                }
            }).execute();


        }
    }

    private boolean isEmailValid(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show)
//    {
//
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
//        {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
//            {
//                @Override
//                public void onAnimationEnd(Animator animation)
//                {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
//            {
//                @Override
//                public void onAnimationEnd(Animator animation)
//                {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        }
//        else
//        {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }
    public void reGister(View view)
    {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void forGetPassword(View view)
    {
        // TODO: forget password functionality here..

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
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
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

