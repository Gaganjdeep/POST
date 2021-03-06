package com.ariseden.post.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariseden.post.activities.ViewImageActivity;

import java.util.List;
import com.ariseden.post.R;

import com.ariseden.post.widget.RoundedCornersGaganImg;

/**
 * Created by gagandeep on 19 Apr 2016.
 */
public class WallFameAdapter extends RecyclerView.Adapter<WallFameAdapter.HomeViewHolders>
{
    private List<WallOfFameModel> itemList;
    private Context               context;

    public WallFameAdapter(Context context, List<WallOfFameModel> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public HomeViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.wall_of_fameinflator, null);
        HomeViewHolders rcv = new HomeViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final HomeViewHolders holder, int position)
    {
        WallOfFameModel currentData = itemList.get(position);

        holder.tvThemeName.setText("Theme : " + currentData.getThemeName());


        if (!currentData.getWallFamePhoto().isEmpty())
        {
            holder.layoutNoImage.setVisibility(View.GONE);
            holder.imgVpost.setVisibility(View.VISIBLE);


            holder.imgVpost.setImageUrlWall(context, currentData.getWallFamePhoto());

            holder.imgVpost.setTag(currentData);
            holder.imgVpost.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    WallOfFameModel model = ((WallOfFameModel) view.getTag());

                    HomeModel homemodel = new HomeModel();
                    homemodel.setImagePath(model.getWallFamePhoto());
                    homemodel.setCreatedDate(model.getThemeStartDate());
                    homemodel.setCustomerId(model.getCustomerId());
                    homemodel.setThemePostId(model.getThemePostId());
                    homemodel.setThemeId(model.getThemeID());

                    Intent intent = new Intent(context, ViewImageActivity.class);
                    intent.putExtra("data", homemodel);
                    context.startActivity(intent);

                }
            });


        }
        else
        {
            holder.layoutNoImage.setVisibility(View.VISIBLE);
            holder.imgVpost.setVisibility(View.GONE);

            holder.tvdate.setText(currentData.getThemeEndDate());

        }

    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }


    public class HomeViewHolders extends RecyclerView.ViewHolder
    {
        public RoundedCornersGaganImg imgVpost;

        public TextView tvThemeName, tvdate;

        LinearLayout layoutNoImage;


        public HomeViewHolders(View itemView)
        {
            super(itemView);
            imgVpost = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgVpost);

            tvThemeName = (TextView) itemView.findViewById(R.id.tvThemeName);
            tvdate = (TextView) itemView.findViewById(R.id.tvdate);
            layoutNoImage = (LinearLayout) itemView.findViewById(R.id.layoutNoImage);
        }


    }


}