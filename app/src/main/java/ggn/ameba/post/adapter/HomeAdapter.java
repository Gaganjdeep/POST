package ggn.ameba.post.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import ggn.ameba.post.R;
import ggn.ameba.post.activities.SettingsActivity;
import ggn.ameba.post.activities.ViewImageActivity;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

/**
 * Created by gagandeep on 14 Apr 2016.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolders>
{
    private List<HomeModel> itemList;
    private Context         context;

    public HomeAdapter(Context context, List<HomeModel> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public HomeViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_adapterlayout, null);
        HomeViewHolders rcv = new HomeViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final HomeViewHolders holder, int position)
    {

        holder.view.setTag(position);

        if (position == 0 || (Integer) holder.view.getTag() % 7 == 0)
        {

            holder.imgVpost.setImageResource(R.drawable.default_grey);
            int                       height = 304;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
            holder.imgVpost.setLayoutParams(params);
        }
        else
        {
            holder.imgVpost.setImageResource(R.drawable.default_grey);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
            holder.imgVpost.setLayoutParams(params);
        }

        holder.imgVpost.setImageUrlSmall(context, itemList.get(position).getImagePath());
        holder.imgVpost.setTag(itemList.get(position));
        holder.imgVpost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Bundle bundle = new Bundle();
//                int[] xy = new int[2];
//
//                view.getLocationOnScreen(xy);
//
//                bundle.putInt("X", xy[0] + (view.getWidth() / 2));
//                bundle.putInt("Y", xy[1] + (view.getHeight() / 2));
//                bundle.putInt("XH", view.getWidth());
//                bundle.putInt("YH", view.getHeight());
//
//                Intent startnextactivity = new Intent(context,ViewImageActivity.class);
//                startnextactivity.putExtras(bundle);
//                context.startActivity(startnextactivity);
                Intent intent = new Intent(context, ViewImageActivity.class);
                intent.putExtra("data", (HomeModel) view.getTag());
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }


    public class HomeViewHolders extends RecyclerView.ViewHolder
    {
        public RoundedCornersGaganImg imgVpost;

        View view;

        public HomeViewHolders(View itemView)
        {
            super(itemView);
            view = itemView;
            imgVpost = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgVpost);

        }


    }


}