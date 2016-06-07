package ggn.ameba.post.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;

public class Email_Verification extends BaseActivityG implements View.OnClickListener
{

    EditText ed_code, ed_email;
    Button btnsubmit, btn_resend;
    ImageButton  btnupdate;
    //    String       currentCase;
    LinearLayout linear_case_forgot_emailId, linear_case_verification_edcode;
//    SharedPreferences sharedPref;

//    Intent intent;


    public void findViewByIDs()
    {
        ed_code = (EditText) findViewById(R.id.ed_code);
        ed_code = (EditText) findViewById(R.id.ed_code);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btnupdate = (ImageButton) findViewById(R.id.btnupdate);
        btn_resend = (Button) findViewById(R.id.btn_resend);
        ed_email = (EditText) findViewById(R.id.ed_email);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_email__verification);

        findViewByIDs();
        btnupdate.setVisibility(View.GONE);

        ed_email.setText(getLocaldata().getEmail());

        linear_case_forgot_emailId = (LinearLayout) findViewById(R.id.linear_case_forgot_emailId);
        linear_case_verification_edcode = (LinearLayout) findViewById(R.id.linear_case_verification_edcode);

        btnupdate.setOnClickListener(this);
        btn_resend.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);


        linear_case_forgot_emailId.setVisibility(View.VISIBLE);
        linear_case_verification_edcode.setVisibility(View.VISIBLE);
        btn_resend.setVisibility(View.VISIBLE);


        ed_email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (!getLocaldata().getEmail().equals(ed_email.getText().toString()))
                {
//                    btnupdate.setEnabled(true);
                    btnupdate.setVisibility(View.VISIBLE);
                }
                else
                {
//                    UtillG.showToast("Email is not changed by user.", Email_Verification.this, true);
                    btnupdate.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

    }


// Starting of on click listener


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.btnsubmit:
            {

                if (!ed_code.getText().toString().isEmpty())
                {
                    showProgress();


                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("EmailVerifyCode", ed_code.getText().toString().trim());
                    hashMap.put("CustomerId", getLocaldata().getUserid());

//                    {"CustomerId" : 101, "EmailVerifyCode" : 2345 }
                    new SuperAsyncG(GlobalConstantsG.URL + "Customer/ValidateEmailCode", hashMap, new CallBackG<String>()
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

                                    UtillG.showToast(jboj.getString(GlobalConstantsG.Message), Email_Verification.this, true);

                                    Intent i = new Intent(Email_Verification.this, HomeTabActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    UtillG.showToast(jboj.getString(GlobalConstantsG.Message), Email_Verification.this, true);
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
                else
                {
                    UtillG.showToast("Please fill verification code.", Email_Verification.this, true);

                }

                break;
            }
            case R.id.btn_resend:
            {
                if (!ed_email.getText().toString().equals(getLocaldata().getEmail()))
                {
                    UtillG.show_dialog_msg(Email_Verification.this, "Your have edited your Email Id, Please click on update button to submit the code for new Email ID.", null);
                }
                else
                {
                    if (!ed_email.getText().toString().isEmpty())
                    {
                        showProgress();


                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("EmailId", ed_email.getText().toString().trim());


                        new SuperAsyncG(GlobalConstantsG.URL + "Customer/ResendEmailVerifyCode", hashMap, new CallBackG<String>()
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
                                        UtillG.show_dialog_msg(Email_Verification.this, "Verification code is sent to your email.", null);
                                    }
                                    else
                                    {
                                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), Email_Verification.this, true);
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
                    else
                    {
                        UtillG.showToast("Please enter your email.", Email_Verification.this, true);
                    }
                }
                break;
            }
            case R.id.btnupdate:
            {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString().trim()).matches())
                {
//                    new Update_email_service(Email_Verification.this, ed_email.getText().toString().trim()).execute();
                    showProgress();


                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("EmailId", ed_email.getText().toString().trim());
                    hashMap.put("CustomerId", getLocaldata().getUserid());


                    new SuperAsyncG(GlobalConstantsG.URL + "Customer/UpdateEmail", hashMap, new CallBackG<String>()
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
                                    UtillG.show_dialog_msg(Email_Verification.this, jboj.getString(GlobalConstantsG.Message), null);

                                    getLocaldata().setEmail(ed_email.getText().toString().trim());


                                    btnupdate.setVisibility(View.GONE);
                                }
                                else
                                {
                                    UtillG.showToast(jboj.getString(GlobalConstantsG.Message), Email_Verification.this, true);
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
                else
                {
                    UtillG.show_dialog_msg(Email_Verification.this, "Your Email is not Valid. please try again.", null);
                }
            }
        }

    }


// ending of on click listener


}