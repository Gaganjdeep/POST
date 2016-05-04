package ggn.ameba.post.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import ggn.ameba.post.R;

/**
 * Created by gagandeep on 19 Apr 2016.
 */
public class WallFameAdapter extends RecyclerView.Adapter<WallFameAdapter.HomeViewHolders>
{
    private List<String> itemList;
    private Context      context;

    public WallFameAdapter(Context context, List<String> itemList)
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


    }

    @Override
    public int getItemCount()
    {
        return 20;
    }


    public class HomeViewHolders extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        public ImageView imgVpost;


        public HomeViewHolders(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            imgVpost = (ImageView) itemView.findViewById(R.id.imgVpost);

        }

        @Override
        public void onClick(View view)
        {
//            Toast.makeText(view.getContext(),
//                    "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
//                    .show();
        }
    }


}