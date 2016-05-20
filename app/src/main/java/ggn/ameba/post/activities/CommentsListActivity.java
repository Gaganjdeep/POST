package ggn.ameba.post.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import ggn.ameba.post.UtillsG.SharedPrefHelper;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.widget.RoundedCornersGaganImg;


public class CommentsListActivity extends Activity
{


    private EditText     edComment;
    private RecyclerView recyclerList;

    private TextView tvNoComment;

    private CommentsAdapter     commentsAdapter;
    private List<CommentsModel> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ThemePostId = getIntent().getStringExtra("ThemePostId");
        ThemeId = getIntent().getStringExtra("ThemeId");
        localdata = new SharedPrefHelper(getApplicationContext());

//        settingActionBar();

        findViewByID();
        hitWebserviceG();


    }


  /*  private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }


    void findViewByID()
    {

        listData = new ArrayList<>();

        tvNoComment = (TextView) findViewById(R.id.tvNoComment);
        edComment = (EditText) findViewById(R.id.edComment);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentsListActivity.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerList.setLayoutManager(linearLayoutManager);

        commentsAdapter = new CommentsAdapter(CommentsListActivity.this, listData);


        recyclerList.setAdapter(commentsAdapter);


    }

    SharedPrefHelper localdata;

    public SharedPrefHelper getLocaldata()
    {
        return localdata;
    }


    Dialog dialog;

    public void showProgress()
    {
        dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        dialog.show();

    }

    public void cancelProgress()
    {
        if (dialog != null)
        {
            dialog.cancel();
        }
    }


    void hitWebserviceG()
    {
        try
        {
            showProgress();
            HashMap<String, String> data = new HashMap<>();
//            http://112.196.34.42:8089/theme/GetPostAllComments?ThemeID=38&ThemePostId=11
            new SuperAsyncG(GlobalConstantsG.URL + "theme/GetPostAllComments?ThemeID=" + ThemeId + "&ThemePostId=" + ThemePostId, data, new CallBackG<String>()
            {
                @Override
                public void callBack(String output)
                {
                    try
                    {
                        cancelProgress();

                        JSONObject jsonMain = new JSONObject(output);

                        if (jsonMain.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                        {


                            JSONArray jsonarrayData = jsonMain.getJSONArray(GlobalConstantsG.Message);


                            if (jsonarrayData.length() > 0)
                            {
                                tvNoComment.setVisibility(View.GONE);
                            }
                            {
                                tvNoComment.setVisibility(View.VISIBLE);
                            }


                            for (int g = 0; g < jsonarrayData.length(); g++)
                            {
                                JSONObject jobj = jsonarrayData.optJSONObject(g);
//                                "PostCommentId":​2,
//                                    "ThemePostId":​11,
//                                    "ThemeID":​38,
//                                    "CustomerId":​587,
//                                    "Comment":"Great pics",
//                                    "CommentDate":"2016-05-18T10:33:23.98",
//                                    "CustomerName":"testing a850",
//                                    "PhotoPath":
//                                "http://112.196.34.42:8089/CustomerPhoto/65153f3d-803c-41aa-ae1f-e3335d2b1ccb.png
//

                                String name        = jobj.optString("CustomerName");
                                String cmntmsg     = jobj.optString("Comment");
                                String comntId     = jobj.optString("PostCommentId");
                                String userid      = jobj.optString("CustomerId");
                                String create_date = jobj.optString("CommentDate");

                                boolean ismyComment = userid.equals(getLocaldata().getUserid());

                                CommentsModel data = new CommentsModel(name, cmntmsg, comntId, jobj.optString("PhotoPath"), userid, create_date, ismyComment);

                                listData.add(data);
                            }


                            Collections.reverse(listData);

                            commentsAdapter.notifyDataSetChanged();

                        }
                        else
                        {
                            tvNoComment.setVisibility(View.VISIBLE);
                        }

                    }
                    catch (Exception e)
                    {
                        tvNoComment.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    String ThemePostId = "", ThemeId = "";
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtilsG.SEVER_FORMAT);

    public void postComment(View view)
    {
        try
        {


            if (edComment.getText().toString().trim().isEmpty())
            {
                return;
            }

            showProgress();

//            {"ThemePostId":11, "ThemeID" : 38, "CustomerId" : 123 , "Comment" : "Great pics"}
            HashMap<String, String> data = new HashMap<>();
            data.put("CustomerId", getLocaldata().getUserid());
            data.put("Comment", edComment.getText().toString());
            data.put("ThemePostId", ThemePostId);
            data.put("ThemeID", ThemeId);

            new SuperAsyncG(GlobalConstantsG.URL + "theme/SavePostComment", data, new CallBackG<String>()
            {
                @Override
                public void callBack(String output)
                {

                    cancelProgress();

                    try
                    {

                        JSONObject jboj = new JSONObject(output);

                        if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                        {
                            String name    = getLocaldata().getName();
                            String cmntmsg = edComment.getText().toString();
                            String comntId = "";


                            CommentsModel data = new CommentsModel(name, cmntmsg, comntId, getLocaldata().getPhotoUrl(), getLocaldata().getUserid(), sdf.format(new Date(System.currentTimeMillis())), true);

                            listData.add(0, data);

                            commentsAdapter.notifyDataSetChanged();

                            edComment.setText("");

                        }
                        else
                        {
                            UtillG.showToast(jboj.getString(GlobalConstantsG.Message), CommentsListActivity.this, true);
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    public class CommentsModel
    {
        private String name, commentmsg, commentid, imgUrl, userid, create_date;

        private boolean isMyComment;

        public CommentsModel(String name, String commentmsg, String commentid, String imgUrl, String userid, String create_date, boolean isMyComment)
        {
            this.name = name;
            this.commentmsg = commentmsg;
            this.commentid = commentid;
            this.imgUrl = imgUrl;
            this.userid = userid;
            this.create_date = create_date;
            this.isMyComment = isMyComment;
        }

        public String getImgUrl()
        {
            return imgUrl;
        }

        public String getUserid()
        {
            return userid;
        }

        public String getCreate_date()
        {
            return create_date;
        }

        public String getName()
        {
            return name;
        }

        public String getCommentmsg()
        {
            return commentmsg;
        }

        public String getCommentid()
        {
            return commentid;
        }

        public boolean isMyComment()
        {
            return isMyComment;
        }
    }


    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolderG>

    {
        private LayoutInflater inflater;

        Context con;

        private List<CommentsModel> dataList;


        public CommentsAdapter(Context context, List<CommentsModel> dList)
        {

            this.dataList = dList;
            con = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public CommentsAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(R.layout.comments_inflator, parent, false);
            return new MyViewHolderG(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolderG holder, int position)
        {

            final CommentsModel currentData = dataList.get(position);

            if (tvNoComment.getVisibility() == View.VISIBLE)
            {
                tvNoComment.setVisibility(View.GONE);
            }


            holder.tvName.setText(currentData.getName());
            holder.tvText.setText(currentData.getCommentmsg());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
            params.gravity = Gravity.TOP;
            holder.imgUserPic.setLayoutParams(params);

            holder.imgUserPic.setImageUrlRoundSmall(con, currentData.getImgUrl());


            try
            {
                holder.tvtime.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf       = new SimpleDateFormat(DateUtilsG.SEVER_FORMAT);
                SimpleDateFormat sdfDesire = new SimpleDateFormat("dd MMM hh:mm a");
                Date             date      = sdf.parse(currentData.getCreate_date());
                holder.tvtime.setText(sdfDesire.format(date));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            holder.imgUserPic.setTag(currentData.getUserid());
            holder.tvName.setTag(currentData.getUserid());

           /* holder.view.setTag(currentData);
            holder.view.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    final CommentsModel commentsModel = (CommentsModel) v.getTag();


                    if (commentsModel.isMyComment())
                    {
                        PopupMenu popup = new PopupMenu(con, v);

                        SpannableString s = new SpannableString("Delete comment");
                        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                        popup.getMenu().add(s);

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                        {
                            @Override
                            public boolean onMenuItemClick(MenuItem item)
                            {
                                delComment(commentsModel);
                                return false;
                            }
                        });
                        popup.show();
                    }

                    return false;
                }
            });*/


        }


        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


        class MyViewHolderG extends RecyclerView.ViewHolder
        {
            TextView tvName, tvText, tvtime;
            View                   view;
            RoundedCornersGaganImg imgUserPic;

            public MyViewHolderG(View itemView)
            {
                super(itemView);
                tvtime = (TextView) itemView.findViewById(R.id.tvtime);
                tvText = (TextView) itemView.findViewById(R.id.tvText);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                imgUserPic = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgUserPic);
                view = itemView;
            }
        }

    }


   /* private void delComment(final CommentsModel cmntId)
    {
        try
        {
            showProgress();

            HashMap<String, String> data = new HashMap();
            data.put("comment_id", cmntId.getCommentid());

            new SuperAsyncG(GlobalConstantsG.URL + "deletecomment", data, new CallBackG<String>()
            {
                @Override
                public void callBack(String output)
                {

                    cancelProgress();

                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
                        {
                            listData.remove(cmntId);
                            commentsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            UtillG.showToast("Unable to delete comment", CommentsListActivity.this, true);
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }*/


}