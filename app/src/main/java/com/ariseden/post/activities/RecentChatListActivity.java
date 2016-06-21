package com.ariseden.post.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ariseden.post.UtillsG.GlobalConstantsG;
import com.ariseden.post.WebService.SuperAsyncG;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ariseden.post.R;
import com.ariseden.post.UtillsG.CallBackG;
import com.ariseden.post.UtillsG.DateUtilsG;
import com.ariseden.post.UtillsG.UtillG;
import com.ariseden.post.adapter.RecentChatAdapter;
import com.ariseden.post.adapter.RecentChatModel;

public class RecentChatListActivity extends BaseActivityG
{
  private  RecyclerView recyclerList;


    List<RecentChatModel> listData;

    RecentChatAdapter recentChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chat_list);
        settingActionBar();

        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(RecentChatListActivity.this));

        listData = new ArrayList<>();


        recentChatAdapter = new RecentChatAdapter(RecentChatListActivity.this, listData);
        recyclerList.setAdapter(recentChatAdapter);


    }


    @Override
    protected void onResume()
    {
        getRecentChatList();
        super.onResume();
    }

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recent Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.arrow_white);
    }

    SimpleDateFormat sdf = new SimpleDateFormat(DateUtilsG.G_FORMAT);

    private void getRecentChatList()
    {


        showProgress();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("CustomerIdBy", getLocaldata().getUserid());


        new SuperAsyncG(GlobalConstantsG.URL + "Chat/GetUserChatList", hashMap, new CallBackG<String>()
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

                        JSONArray jsonarray = jboj.getJSONArray("Message");

                        listData.clear();

                        for (int i = 0; i < jsonarray.length(); i++)
                        {
                            JSONObject jobjInner = jsonarray.getJSONObject(i);

                            RecentChatModel recentChatModel = new RecentChatModel();
                            recentChatModel.setCustomerIdTo(jobjInner.optString("CustomerIdTo"));
                            recentChatModel.setCustomerName(jobjInner.optString("CustomerName"));
                            recentChatModel.setChatContent(jobjInner.optString("ChatContent"));

                            String date = jobjInner.optString("DateTimeCreated");
                            if (date.contains("."))
                            {
                                date = date.substring(0, date.indexOf("."));
                            }

                            String created_at = sdf.format(DateUtilsG.dateServerChat(date));
                            recentChatModel.setDateTimeCreated(created_at);


                            recentChatModel.setPhotoPath(jobjInner.optString("PhotoPath"));
                            recentChatModel.setIsRead(jobjInner.optString("IsRead"));

                            listData.add(recentChatModel);
                        }

                        recentChatAdapter.notifyDataSetChanged();

                    }
                    else
                    {
                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), RecentChatListActivity.this, true);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
