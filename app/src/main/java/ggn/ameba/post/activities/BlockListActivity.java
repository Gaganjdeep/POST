package ggn.ameba.post.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.adapter.RecentChatModel;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

public class BlockListActivity extends BaseActivityG
{
    private RecyclerView recyclerList;


    private List<RecentChatModel> listData;

    private BlockLIstAdapter blockLIstAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chat_list);
        settingActionBar();

        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(BlockListActivity.this));

        listData = new ArrayList<>();


        blockLIstAdapter = new BlockLIstAdapter(BlockListActivity.this, listData);
        recyclerList.setAdapter(blockLIstAdapter);


        getRecentChatList();

    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blocked Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.arrow_white);
    }

    private void getRecentChatList()
    {
        showProgress();

        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("CustomerIdBy", getLocaldata().getUserid());

        new SuperAsyncG(GlobalConstantsG.URL + "Customer/GetBlockList?CustomerId=" + getLocaldata().getUserid(), hashMap, new CallBackG<String>()
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
                            JSONObject      jobjInner       = jsonarray.getJSONObject(i);
                            RecentChatModel recentChatModel = new RecentChatModel();
                            recentChatModel.setCustomerIdTo(jobjInner.optString("CustomerIdTo"));
                            recentChatModel.setCustomerName(jobjInner.optString("CustomerNameTo"));
                            recentChatModel.setPhotoPath(jobjInner.optString("PhotoPathTo"));

                            listData.add(recentChatModel);
                        }

                        blockLIstAdapter.notifyDataSetChanged();

                    }
                    else
                    {
                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), BlockListActivity.this, true);
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


    public class BlockLIstAdapter extends RecyclerView.Adapter<BlockLIstAdapter.HomeViewHolders>
    {
        private List<RecentChatModel> itemList;
        private Context               context;

        public BlockLIstAdapter(Context context, List<RecentChatModel> itemList)
        {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public HomeViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.block_list_inflater, parent, false);
            HomeViewHolders rcv = new HomeViewHolders(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(final HomeViewHolders holder, int position)
        {

            RecentChatModel recentChatModel = itemList.get(position);

            holder.RCGI_usrImg.setImageUrlRoundSmall(context, recentChatModel.getPhotoPath());
            holder.txtv_blockedName.setText(recentChatModel.getCustomerName());

            holder.btnUnblock.setTag(recentChatModel);
            holder.btnUnblock.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View view)
                {

                    final RecentChatModel recentChatModel = (RecentChatModel) view.getTag();

                    showProgress();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("CustomerIdBy", getLocaldata().getUserid());
                    hashMap.put("CustomerIdTo", recentChatModel.getCustomerIdTo());
                    hashMap.put("IsBlocked", "0");


                    new SuperAsyncG(GlobalConstantsG.URL + "Customer/SaveBlockUnblock", hashMap, new CallBackG<String>()
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
                                    UtillG.showToast("User Unblocked.", context, true);
                                    listData.remove(recentChatModel);
                                    notifyDataSetChanged();
                                }
                                else
                                {
                                    UtillG.showToast(jboj.getString(GlobalConstantsG.Message), context, true);
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }


                        }
                    }).execute();

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

            private TextView txtv_blockedName, btnUnblock;
            private RoundedCornersGaganImg RCGI_usrImg;

            public HomeViewHolders(View itemView)
            {
                super(itemView);
                RCGI_usrImg = (RoundedCornersGaganImg) itemView.findViewById(R.id.RCGI_usrImg);
                txtv_blockedName = (TextView) itemView.findViewById(R.id.txtv_blockedName);
                btnUnblock = (TextView) itemView.findViewById(R.id.btnUnblock);

            }


        }


    }


}