package ggn.ameba.post.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.DateUtilsG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.adapter.ChatMsgAdapter;
import ggn.ameba.post.adapter.MsgDataModel;
import ggn.ameba.post.adapter.RecentChatModel;
import ggn.ameba.post.widget.EndlessRecyclerOnScrollListener;

public class ChatActivity extends BaseActivityG
{

    RecyclerView   recyclerList;
    EditText       edComment;
    ChatMsgAdapter chatMsgAdapter;
    ProgressBar    progressBar;

    List<MsgDataModel> listData;

    RecentChatModel OtherUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        settingActionBar();


        receiver = new Chat_BroadcastReceiver();

        OtherUserData = (RecentChatModel) getIntent().getSerializableExtra("data");

        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        LinearLayoutManager layout = new LinearLayoutManager(ChatActivity.this);
        layout.setReverseLayout(true);
        recyclerList.setLayoutManager(layout);

        edComment = (EditText) findViewById(R.id.edComment);

        listData = new ArrayList<>();
        chatMsgAdapter = new ChatMsgAdapter(ChatActivity.this, listData);
        recyclerList.setAdapter(chatMsgAdapter);


        PageNumber = 0;

        getMsg(PageNumber + "");


        recyclerList.setOnScrollListener(new EndlessRecyclerOnScrollListener(layout)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                if (listData.size() > 14)
                {
                    if (!isLoading)
                    {
                        // do something...
                        isLoading(true);

//                    PageNumber = current_page;


                        getMsg(PageNumber + "");
                    }


                }


            }
        });


    }


    boolean isLoading = false;


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat");
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


    SimpleDateFormat sdf = new SimpleDateFormat(DateUtilsG.G_FORMAT);

    public void sendMsg(View view)
    {
        showMsg();
    }


    private void isLoading(boolean isloading)
    {
        this.isLoading = isloading;
        progressBar.setVisibility(isloading ? View.VISIBLE : View.GONE);
    }


    private void showMsg()
    {
        try
        {


            if (edComment.getText().toString().trim().isEmpty())
            {
                return;
            }

            sendChat(edComment.getText().toString().trim());

            String id               = "";
            String sender_userid    = getLocaldata().getUserid();
            String recipient_userid = "597";
            String message          = edComment.getText().toString().trim();

            String created_at = sdf.format(new Date(System.currentTimeMillis()));


            String username = getLocaldata().getName();


            String profile_pic = getLocaldata().getPhotoUrl();

            MsgDataModel msgDataModel = new MsgDataModel();

            msgDataModel.setLastMsg(true);
            msgDataModel.setId(id);
            msgDataModel.setSender_userid(sender_userid);
            msgDataModel.setRecipient_userid(recipient_userid);
            msgDataModel.setMessage(message);
            msgDataModel.setCreated_at(created_at);
            msgDataModel.setUsername(username);
            msgDataModel.setProfile_pic(profile_pic);

            listData.add(0, msgDataModel);

            edComment.setText("");
            chatMsgAdapter.notifyDataSetChanged();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void sendChat(String text)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("CustomerIdBy", getLocaldata().getUserid());
        hashMap.put("CustomerIdTo", OtherUserData.getCustomerIdTo());
        hashMap.put("ChatContent", text);

        new SuperAsyncG(GlobalConstantsG.URL + "Chat/SaveChat", hashMap, new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {

                    }
                    else
                    {
                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), ChatActivity.this, true);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        }).execute();


    }


    private void getUnread()
    {

        HashMap<String, String> input = new HashMap<>();
        input.put("CustomerIdBy", OtherUserData.getCustomerIdTo());
        input.put("CustomerIdTo", getLocaldata().getUserid());

        new SuperAsyncG(GlobalConstantsG.URL + "Chat/GetChatListUnread", input, new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {

                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.optString("Status").equals("success"))
                    {
                        if (PageNumber == 0)
                        {
                            listData.clear();
                        }

                        JSONArray Jarray = mainobj.optJSONArray("Message");
                        newMessagesCount = Jarray.length();
                        for (int i = 0; i < Jarray.length(); i++)
                        {
                            JSONObject innerobj = Jarray.optJSONObject(i);

                            boolean isSender = getLocaldata().getUserid().equals(innerobj.optString("CustomerIdBy"));


                            String date = innerobj.optString("DateTimeCreated");
                            if (date.contains("."))
                            {
                                date = date.substring(0, date.indexOf("."));
                            }

                            String created_at = sdf.format(DateUtilsG.dateServer(date));

                            String profile_pic = isSender ? getLocaldata().getPhotoUrl() : OtherUserData.getPhotoPath();
                            String username    = isSender ? getLocaldata().getName() : OtherUserData.getCustomerName();


                            MsgDataModel msgDataModel = new MsgDataModel();

                            msgDataModel.setLastMsg(false);
                            msgDataModel.setId(innerobj.optString("ChatId"));
                            msgDataModel.setSender_userid(innerobj.optString("CustomerIdBy"));
                            msgDataModel.setRecipient_userid(innerobj.optString("CustomerIdTo"));
                            msgDataModel.setMessage(innerobj.optString("ChatContent"));
                            msgDataModel.setCreated_at(created_at);
                            msgDataModel.setUsername(username);
                            msgDataModel.setProfile_pic(profile_pic);


                            edComment.setText("");

                            listData.add(0, msgDataModel);

                        }

                        chatMsgAdapter.notifyDataSetChanged();

                    }
                    else
                    {
                        UtillG.showToast(mainobj.optString("Message"), ChatActivity.this, true);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


        ).

                execute();
    }

    int newMessagesCount = 0;
    int PageNumber       = 0;

    private void getMsg(String pageNumber)
    {
        HashMap<String, String> input = new HashMap<>();
        input.put("CustomerIdTo", OtherUserData.getCustomerIdTo());
        input.put("CustomerIdBy", getLocaldata().getUserid());
        input.put("PageNumber", pageNumber);
        new SuperAsyncG(GlobalConstantsG.URL + "Chat/GetChatListV1", input, new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
                    isLoading(false);
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.optString("Status").equals("success"))
                    {
                        if (PageNumber == 0)
                        {
                            listData.clear();
                        }

                        JSONArray Jarray = mainobj.optJSONArray("Message");
                        newMessagesCount = Jarray.length();


                        List<MsgDataModel> tempList = new ArrayList<MsgDataModel>();


                        for (int i = 0; i < Jarray.length(); i++)
                        {
//                            HashMap<String, String> innerhash = new HashMap<>();
                            JSONObject innerobj = Jarray.optJSONObject(i);
//                            innerhash.put("ChatId", innerobj.optString("ChatId"));
//                            innerhash.put("CustomerIdBy", innerobj.optString("CustomerIdBy"));
//                            innerhash.put("CustomerIdTo", innerobj.optString("CustomerIdTo"));
//                            innerhash.put("ChatContent", innerobj.optString("ChatContent"));
//                            innerhash.put("DateTimeCreated", innerobj.optString("DateTimeCreated"));
//                            innerhash.put("ImagePath", innerobj.optString("ImagePath"));
//                            innerhash.put("IsRead", innerobj.optString("IsRead"));
//                            innerhash.put("IsDeletedCustomerBy", innerobj.optString("IsDeletedCustomerBy"));
//                            innerhash.put("IsDeletedCustomerTo", innerobj.optString("IsDeletedCustomerTo"));
//                            innerhash.put("CountMessage", innerobj.optString("CountMessage"));
//                            innerhash.put("PageNumber", innerobj.optString("PageNumber"));
//                            innerhash.put("RandomChatId", innerobj.optString("RandomChatId"));
//                            innerhash.put("isURI", "no");

                            boolean isSender = getLocaldata().getUserid().equals(innerobj.optString("CustomerIdBy"));

                            String date = innerobj.optString("DateTimeCreated");
                            if (date.contains("."))
                            {
                                date = date.substring(0, date.indexOf("."));
                            }

                            String created_at = sdf.format(DateUtilsG.dateServer(date));

                            String profile_pic = isSender ? getLocaldata().getPhotoUrl() : OtherUserData.getPhotoPath();
                            String username    = isSender ? getLocaldata().getName() : OtherUserData.getCustomerName();

                            MsgDataModel msgDataModel = new MsgDataModel();

                            msgDataModel.setLastMsg(false);
                            msgDataModel.setId(innerobj.optString("ChatId"));
                            msgDataModel.setSender_userid(innerobj.optString("CustomerIdBy"));
                            msgDataModel.setRecipient_userid(innerobj.optString("CustomerIdTo"));
                            msgDataModel.setMessage(innerobj.optString("ChatContent"));
                            msgDataModel.setCreated_at(created_at);
                            msgDataModel.setUsername(username);
                            msgDataModel.setProfile_pic(profile_pic);

                            if (PageNumber == 0)
                            {
                                if (!listData.contains(msgDataModel))
                                {
                                    listData.add(0, msgDataModel);

                                }
                                tempList.clear();
                            }
                            else
                            {
                                tempList.add(msgDataModel);
//                                listData.add(listData.size(), msgDataModel);
                            }
                        }


                        if (tempList.size() > 0)
                        {
                            Collections.reverse(tempList);
                            for (MsgDataModel msg : tempList)
                            {
                                if (!listData.contains(msg))
                                {
                                    listData.add(listData.size(), msg);
                                }
                            }


                        }

                        chatMsgAdapter.notifyDataSetChanged();
                        PageNumber++;
                    }
                    else
                    {
                        UtillG.showToast(mainobj.optString("Message"), ChatActivity.this, true);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    boolean broadcastisnull = false;

    @Override
    protected void onResume()
    {
        super.onResume();

        if (!broadcastisnull)
        {
            if (receiver == null)
            {
                receiver = new Chat_BroadcastReceiver();
            }

            registerReceiver(receiver, new IntentFilter(GlobalConstantsG.BROADCAST_GETUNREAD));
            broadcastisnull = true;
        }

    }


    @Override
    protected void onPause()
    {
        try
        {
            if (broadcastisnull)
            {
                unregisterReceiver(receiver);
                broadcastisnull = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onPause();
    }

    Chat_BroadcastReceiver receiver;

    private class Chat_BroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getUnread();
        }
    }


}
