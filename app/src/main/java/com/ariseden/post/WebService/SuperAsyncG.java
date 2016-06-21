package com.ariseden.post.WebService;

import android.os.AsyncTask;

import java.util.HashMap;

import com.ariseden.post.UtillsG.CallBackG;

/**
 * Created by gagandeep on 28/9/15.
 */
public class SuperAsyncG extends AsyncTask<Void, String, String>
{

    private String                  URL_;
    private HashMap<String, String> hashMapData;
    private CallBackG               callBack;


    public static SuperAsyncG  asyncClass;


    public SuperAsyncG(String URL_, HashMap<String, String> hashMapData, CallBackG callBack)
    {
        this.URL_ = URL_;
        this.hashMapData = hashMapData;
        this.callBack = callBack;


        asyncClass = this;
    }


    public static void cancelAsync()
    {
        if (asyncClass != null)
        {
            asyncClass.cancel(true);
        }
    }


    @Override
    protected String doInBackground(Void... params)
    {


        if (hashMapData.isEmpty())
        {
            return new WebServiceHelper().performGetCall(URL_);
        }
        else
        {
            return new WebServiceHelper().performPostCall(URL_, hashMapData);
        }
    }

    @Override
    protected void onPostExecute(String s)
    {

        callBack.callBack(s);

        super.onPostExecute(s);
    }
}
