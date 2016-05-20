package ggn.ameba.post.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.DateUtilsG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.adapter.ChatMsgAdapter;
import ggn.ameba.post.adapter.MsgDataModel;
import ggn.ameba.post.adapter.RecentChatModel;
import ggn.ameba.post.widget.CircleTransform;
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

        OtherUserData = (RecentChatModel) getIntent().getSerializableExtra("data");
        settingActionBar();


        receiver = new Chat_BroadcastReceiver();


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


        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layout)
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
                        getMsg(PageNumber + "");
                    }


                }


            }
        };

        recyclerList.setOnScrollListener(endlessRecyclerOnScrollListener);


    }

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.clear_chat_menu, menu);

        MenuItem        item = menu.getItem(0);
        SpannableString s    = new SpannableString("Clear Chat");
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        item.setTitle(s);


        MenuItem        item2 = menu.getItem(1);
        SpannableString s2    = new SpannableString("Block User");
        s2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s2.length(), 0);
        item2.setTitle(s2);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        if (item.getItemId() == R.id.clear)
        {

//    URL: ttp://112.196.34.42:8089/Chat/ClearAllChat
//
//    Method:Post
//
//    Header : Content-Type: application/json
//
//    Body-
//    {"CustomerIdBy":267,"CustomerIdTo":264}
//
//    Result/Output:
//
//    {"Status":"success","Message":"Chat is deleted successfully."}
//    or
//    {"Status":"error","Message":"Chat is not deleted."}

            View.OnClickListener onClickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    UtillG.global_dialog.dismiss();


                    showProgress();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("CustomerIdBy", getLocaldata().getUserid());
                    hashMap.put("CustomerIdTo", OtherUserData.getCustomerIdTo());

                    new SuperAsyncG(GlobalConstantsG.URL + "Chat/ClearAllChat", hashMap, new CallBackG<String>()
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

                                    listData.clear();
                                    chatMsgAdapter.notifyDataSetChanged();
                                    endlessRecyclerOnScrollListener.startOverStaggered();

                                    UtillG.showToast("Chat deleted.", ChatActivity.this, true);
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
            };


            if (listData.isEmpty())
            {
                UtillG.showToast("No Chat to clear !", ChatActivity.this, true);
            }
            else
            {
                UtillG.show_dialog_msg(ChatActivity.this, "Are you sure,You want to clear chat ?", onClickListener);
            }


        }
        else if (item.getItemId() == R.id.block)
        {
            View.OnClickListener onClickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    UtillG.global_dialog.dismiss();


                    showProgress();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("CustomerIdBy", getLocaldata().getUserid());
                    hashMap.put("CustomerIdTo", OtherUserData.getCustomerIdTo());
                    hashMap.put("IsBlocked", "1");


                    new SuperAsyncG(GlobalConstantsG.URL + "Customer/SaveBlockUnblock", hashMap,  new CallBackG<String>()
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
                                    UtillG.showToast("User Blocked.", ChatActivity.this, true);
                                    finish();
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
            };


            UtillG.show_dialog_msg(ChatActivity.this, "You want to block " + OtherUserData.getCustomerName() + " ?", onClickListener);


        }
        else
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    boolean isLoading = false;


    private void settingActionBar()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(OtherUserData.getCustomerName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.arrow_white);


        try
        {
//            Drawable drawable = new ScaleDrawable(getResources().getDrawable(R.mipmap.ic_default_pic_rounded), Gravity.LEFT, 30, 30).getDrawable();
//
//            drawable.setBounds(30, 30, 30, 30);
            Drawable dr     = getResources().getDrawable(R.mipmap.ic_default_pic_rounded);
            Bitmap   bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 45, 45, true));

            toolbar.setLogo(d);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (!OtherUserData.getPhotoPath().isEmpty())
        {
            Picasso.with(ChatActivity.this).load(OtherUserData.getPhotoPath()).resize(60, 60).transform(new CircleTransform()).centerInside().priority(Picasso.Priority.HIGH).into(new Target()
            {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                {
                    toolbar.setLogo(new BitmapDrawable(getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable)
                {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable)
                {

                }
            });
        }

    }


    SimpleDateFormat sdf = new SimpleDateFormat(DateUtilsG.G_FORMAT, Locale.US);

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

    int PageNumber = 0;

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
