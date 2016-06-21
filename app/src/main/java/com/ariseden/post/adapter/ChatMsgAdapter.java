package com.ariseden.post.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import com.ariseden.post.R;
import com.ariseden.post.UtillsG.SharedPrefHelper;
import com.ariseden.post.widget.RoundedCornersGaganImg;

/**
 * Created by gagandeep on 20 Apr 2016.
 */
public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.MyViewHolderG>

{

    private LayoutInflater inflater;

    Context con;

    private List<MsgDataModel> dataList;


    SharedPrefHelper sharedPrefHelper;

    public ChatMsgAdapter(Context context, List<MsgDataModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
        sharedPrefHelper = new SharedPrefHelper(con);
    }


    final int MY_MSG    = 1;
    final int OTHER_MSG = 2;


    @Override
    public int getItemViewType(int position)
    {
        if (dataList.get(position).getSender_userid().equals(sharedPrefHelper.getUserid()))
        {
            return MY_MSG;
        }
        else
        {
            return OTHER_MSG;
        }
    }

    @Override
    public ChatMsgAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
//        View view = inflater.inflate(R.layout.custom_other_chat, parent, false);

        if (viewType == MY_MSG)
        {
            return new MyViewHolderG(inflater.inflate(R.layout.custom_my_chat, parent, false));
        }
        else
        {
            return new MyViewHolderG(inflater.inflate(R.layout.custom_other_chat, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {
        MsgDataModel currentData = dataList.get(position);


        holder.tvMSG.setText(currentData.getMessage());


        try
        {
//
//            SimpleDateFormat sdf       = new SimpleDateFormat(GlobalConstantsG.SEVER_FORMAT);
//            SimpleDateFormat sdfDesire = new SimpleDateFormat("hh:mm a");
//            Date             date      = sdf.parse(currentData.getCreated_at());
//

            holder.tvTime.setText(currentData.getCreated_at());


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.DP.setRadius(180);
        holder.DP.setImageUrlRoundSmall(con, currentData.getProfile_pic());
//        holder.DP.setBackgroundResource(R.mipmap.ic_default_pic_rounded);

        holder.view.setTag(currentData);


    }


    /*private void delComment(final MsgDataModel cmntId)
    {
        try
        {

            JSONObject data = new JSONObject();
            data.put("message_id", cmntId.getId());
            data.put("sender_userid", cmntId.getSender_userid());

            new SuperWebServiceG(GlobalConstants.URL + "deletemessage", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            dataList.remove(cmntId);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            Utills.showToast("unable to delete comment", con, true);
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


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView tvMSG, tvTime;
        LinearLayout           chatbubble;
        View                   view;
        RoundedCornersGaganImg DP;

        public MyViewHolderG(View itemView)
        {
            super(itemView);

            tvMSG = (TextView) itemView.findViewById(R.id.tvChatmsg);

            DP = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgChat);
            chatbubble = (LinearLayout) itemView.findViewById(R.id.chatbubble);

            tvTime = (TextView) itemView.findViewById(R.id.tvChatDate);

            view = itemView;
        }
    }

}