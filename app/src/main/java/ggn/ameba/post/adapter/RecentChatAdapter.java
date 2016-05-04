package ggn.ameba.post.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ggn.ameba.post.R;
import ggn.ameba.post.activities.ChatActivity;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

/**
 * Created by gagandeep on 20 Apr 2016.
 */
public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.HomeViewHolders>
{
    private List<RecentChatModel> itemList;
    private Context               context;

    public RecentChatAdapter(Context context, List<RecentChatModel> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public HomeViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recent_chat_inflator, parent, false);
        HomeViewHolders rcv = new HomeViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final HomeViewHolders holder, int position)
    {

        RecentChatModel recentChatModel = itemList.get(position);


        holder.tvUserName.setText(recentChatModel.getCustomerName());
        holder.tvLastMsg.setText(recentChatModel.getChatContent());
        holder.tvTime.setText(recentChatModel.getDateTimeCreated());


    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }


    public class HomeViewHolders extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {

        public TextView tvUserName, tvLastMsg, tvTime;


        public HomeViewHolders(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvLastMsg = (TextView) itemView.findViewById(R.id.tvLastMsg);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);

        }

        @Override
        public void onClick(View view)
        {

            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("data", itemList.get(getPosition()));
            context.startActivity(intent);


//            Toast.makeText(view.getContext(),
//                    "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
//                    .show();


        }
    }


}